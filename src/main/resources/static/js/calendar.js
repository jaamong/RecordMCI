/**
 * calendar.js
├─ 상태
│  ├─ currentYear / currentMonth
│  ├─ selectedDate
│  ├─ monthlyMap
│
├─ 렌더링
│  ├─ renderCalendar()
│  ├─ renderDots()
│
├─ 사용자 액션
│  ├─ selectDate()
│  ├─ moveMonth()
│
├─ 상태 반영 API
│  ├─ updateCalendarDot()     //외부에서 호출
│  └─ refreshDayDotsByDate()
 */

import { renderItems } from "./items/items.js";

let currentYear;
let currentMonth;
let monthlyMap = {}; // date -> record 상태

const USER_ID = 1;

const today = new Date();
const todayYear = today.getFullYear();
const todayMonth = today.getMonth() + 1;
const todayDate = today.getDate();

let selectedDate = null; // yyyy-MM-dd

function initCalendarState() {
  currentYear = todayYear;
  currentMonth = todayMonth;
}

export async function renderCalendar(containerId) {
  // 최초 1회 초기화
  if (!currentYear || !currentMonth) {
    initCalendarState();
  }
  console.log("calendar init:", currentYear, currentMonth);

  const container = document.getElementById(containerId);
  container.innerHTML = "";

  // Monthly API 호출
  const monthlyData = await fetchMonthlyRecords(
    USER_ID,
    currentYear,
    currentMonth
  );

  monthlyMap = {};
  monthlyData.forEach((day) => {
    monthlyMap[day.date] = day;
  });

  const firstDay = new Date(currentYear, currentMonth - 1, 1).getDay();
  const lastDate = new Date(currentYear, currentMonth, 0).getDate();

  /* ===== 헤더 ===== */
  const header = document.createElement("div");
  header.className = "calendar-header";

  const prev = document.createElement("span");
  prev.className = "nav";
  prev.textContent = "<";
  prev.onclick = () => moveMonth(-1);

  const title = document.createElement("span");
  title.className = "month-title";
  title.textContent = `${currentYear}년 ${currentMonth}월`;

  const next = document.createElement("span");
  next.className = "nav";
  next.textContent = ">";
  next.onclick = () => moveMonth(1);

  header.append(prev, title, next);

  /* ===== 요일 ===== */
  const weekdays = document.createElement("div");
  weekdays.className = "calendar-weekdays";

  ["일", "월", "화", "수", "목", "금", "토"].forEach((d, idx) => {
    const el = document.createElement("div");
    el.textContent = d;
    if (idx === 0) el.classList.add("sun");
    if (idx === 6) el.classList.add("sat");
    weekdays.appendChild(el);
  });

  /* ===== 날짜 ===== */
  const daysGrid = document.createElement("div");
  daysGrid.className = "calendar-days";

  for (let i = 0; i < firstDay; i++) {
    daysGrid.appendChild(document.createElement("div"));
  }

  for (let d = 1; d <= lastDate; d++) {
    const dayEl = document.createElement("div");
    dayEl.className = "calendar-day";
    dayEl.textContent = d;

    const dayOfWeek = (firstDay + d - 1) % 7;
    if (dayOfWeek === 0) dayEl.classList.add("sun");
    if (dayOfWeek === 6) dayEl.classList.add("sat");

    const yyyyMMdd = `${currentYear}-${String(currentMonth).padStart(
      2,
      "0"
    )}-${String(d).padStart(2, "0")}`;

    dayEl.onclick = () => selectDate(d);

    // 날짜 아래 점 표시
    const record = monthlyMap[yyyyMMdd];
    if (record) {
      dayEl.appendChild(renderDots(record));
    }

    // 오늘 자동 선택
    if (
      currentYear === todayYear &&
      currentMonth === todayMonth &&
      d === todayDate
    ) {
      dayEl.classList.add("selected");
      selectDate(todayDate);
    }

    daysGrid.appendChild(dayEl);
  }

  container.append(header, weekdays, daysGrid);
}

/* ===== 날짜 선택 ===== */
async function selectDate(day) {
  document
    .querySelectorAll(".calendar-day.selected")
    .forEach((el) => el.classList.remove("selected"));

  const days = document.querySelectorAll(".calendar-day");
  days.forEach((el) => {
    if (Number(el.textContent) === day) {
      el.classList.add("selected");
    }
  });

  selectedDate = `${currentYear}-${String(currentMonth).padStart(
    2,
    "0"
  )}-${String(day).padStart(2, "0")}`;

  console.log("선택 날짜:", selectedDate);

  // DailyRecord 상세 조회
  const record = await fetchDailyRecord(USER_ID, selectedDate);

  renderItems(record, `${currentYear}년 ${currentMonth}월 ${day}일`);
}

/* ===== 월 이동 ===== */
function moveMonth(diff) {
  currentMonth += diff;

  if (currentMonth === 0) {
    currentMonth = 12;
    currentYear--;
  }

  if (currentMonth === 13) {
    currentMonth = 1;
    currentYear++;
  }

  renderCalendar("calendar-area");
}

function createDot(type) {
  const dot = document.createElement("div");
  dot.className = `calendar-dot ${type}`; // food | activity | memo
  return dot;
}

function renderDots(record) {
  const dots = document.createElement("div");
  dots.className = "day-dots";

  if (record.hasFoodConsumed) {
    dots.appendChild(createDot("food"));
  }
  if (record.hasActivityCompleted) {
    dots.appendChild(createDot("activity"));
  }
  if (record.hasMemo) {
    dots.appendChild(createDot("memo"));
  }

  return dots;
}

export function updateCalendarDot(type, enabled) {
  if (!monthlyMap[selectedDate]) return;

  switch (type) {
    case "food":
      monthlyMap[selectedDate].hasFoodConsumed = enabled;
      break;
    case "activity":
      monthlyMap[selectedDate].hasActivityCompleted = enabled;
      break;
    case "memo":
      monthlyMap[selectedDate].hasMemo = enabled;
      break;
  }

  refreshDayDotsByDate(selectedDate);
}

function refreshDayDotsByDate(yyyyMMdd) {
  const day = Number(yyyyMMdd.split("-")[2]);

  const dayEl = Array.from(document.querySelectorAll(".calendar-day")).find(
    (el) => Number(el.textContent) === day
  );

  if (!dayEl) return;

  const oldDots = dayEl.querySelector(".day-dots");
  if (oldDots) oldDots.remove();

  const record = monthlyMap[yyyyMMdd];
  if (!record) return;

  dayEl.appendChild(renderDots(record));
}
