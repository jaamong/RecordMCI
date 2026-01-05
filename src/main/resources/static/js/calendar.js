let currentYear;
let currentMonth;
let monthlyMap = {}; // date -> record 상태

const USER_ID = 1;

const today = new Date();
const todayYear = today.getFullYear();
const todayMonth = today.getMonth() + 1;
const todayDate = today.getDate();

const DOT_COLORS = {
  food: "#5f9b78", // #46855e 보다 살짝 연함
  activity: "#46855e", // 기준
  memo: "#2f5d43", // 가장 진함
};

async function renderCalendar(containerId) {
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
      const dots = document.createElement("div");
      dots.className = "day-dots";

      if (record.hasFoodConsumed) {
        dots.appendChild(createDot(DOT_COLORS.food));
      }
      if (record.hasActivityCompleted) {
        dots.appendChild(createDot(DOT_COLORS.activity));
      }
      if (record.hasMemo) {
        dots.appendChild(createDot(DOT_COLORS.memo));
      }

      dayEl.appendChild(dots);
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

function createDot(className) {
  const dot = document.createElement("div");
  dot.className = `day-dot ${className}`;
  return dot;
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

  const yyyyMMdd = `${currentYear}-${String(currentMonth).padStart(
    2,
    "0"
  )}-${String(day).padStart(2, "0")}`;

  console.log("선택 날짜:", yyyyMMdd);

  // DailyRecord 상세 조회
  const record = await fetchDailyRecord(USER_ID, yyyyMMdd);

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

function createDot(color) {
  const dot = document.createElement("div");
  dot.className = "calendar-dot";
  dot.style.backgroundColor = color;
  return dot;
}

function refreshDayDots(yyyyMMdd) {
  const day = Number(yyyyMMdd.split("-")[2]);

  const dayEls = document.querySelectorAll(".calendar-day");
  dayEls.forEach((dayEl) => {
    if (Number(dayEl.textContent) === day) {
      // 기존 dot 제거
      const oldDots = dayEl.querySelector(".day-dots");
      if (oldDots) oldDots.remove();

      const record = monthlyMap[yyyyMMdd];
      if (!record) return;

      const dots = document.createElement("div");
      dots.className = "day-dots";

      if (record.hasFoodConsumed) {
        dots.appendChild(createDot(DOT_COLORS.food));
      }
      if (record.hasActivityCompleted) {
        dots.appendChild(createDot(DOT_COLORS.activity));
      }
      if (record.hasMemo) {
        dots.appendChild(createDot(DOT_COLORS.memo));
      }

      dayEl.appendChild(dots);
    }
  });
}
