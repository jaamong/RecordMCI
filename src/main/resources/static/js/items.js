function renderItems(record, dateText) {
  const container = document.getElementById("items-area");
  container.innerHTML = "";

  if (!record) {
    container.textContent = "데이터 없음";
    return;
  }

  /* ===== 상단 날짜 ===== */
  const title = document.createElement("div");
  title.className = "section-title";
  title.textContent = dateText;

  container.appendChild(title);

  /* ===== Food ===== */
  container.appendChild(createFoodSection(record.foods));

  /* ===== Activity ===== */
  container.appendChild(createActivitySection(record.activities));

  /* ===== Memo ===== */
  container.appendChild(
    createSection("Memo", record.memo ? [{ name: record.memo }] : [])
  );
}

/* ===== 공통 섹션 ===== */
function createSection(titleText, items) {
  const section = document.createElement("div");

  const header = document.createElement("div");
  header.innerHTML = `<strong>${titleText}</strong> <span>+</span>`;
  header.style.marginTop = "20px";

  section.appendChild(header);

  items.forEach((item) => {
    const row = document.createElement("div");
    row.style.display = "flex";
    row.style.alignItems = "center";
    row.style.marginTop = "8px";

    const checkbox = document.createElement("div");
    checkbox.className = "checkbox";
    if (item.checked) checkbox.classList.add("checked");

    const label = document.createElement("span");
    label.textContent = item.name;

    row.append(checkbox, label);
    section.appendChild(row);
  });

  return section;
}

function createFoodSection(foods) {
  const section = document.createElement("div");

  const header = document.createElement("div");
  header.innerHTML = `<strong>Food</strong> <span>+</span>`;
  header.style.marginTop = "20px";
  section.appendChild(header);

  foods.forEach((food) => {
    const row = document.createElement("div");
    row.style.display = "flex";
    row.style.alignItems = "center";
    row.style.marginTop = "8px";

    const checkbox = document.createElement("div");
    checkbox.className = "checkbox";
    if (food.consumed) checkbox.classList.add("checked");

    checkbox.onclick = async () => {
      try {
        const updated = await updateFoodConsumed(food.id);

        // 성공 시 UI 반영
        if (updated.consumed) {
          checkbox.classList.add("checked");
        } else {
          checkbox.classList.remove("checked");
        }
        await renderCalendar("calendar-area");
      } catch (e) {
        console.error("Food 업데이트 실패", e);
      }
    };

    const label = document.createElement("span");
    label.textContent = food.name;

    row.append(checkbox, label);
    section.appendChild(row);
  });

  return section;
}

function createActivitySection(activities) {
  const section = document.createElement("div");

  const header = document.createElement("div");
  header.innerHTML = `<strong>Activity</strong> <span>+</span>`;
  header.style.marginTop = "20px";
  section.appendChild(header);

  activities.forEach((activity) => {
    const row = document.createElement("div");
    row.style.display = "flex";
    row.style.alignItems = "center";
    row.style.marginTop = "8px";

    const checkbox = document.createElement("div");
    checkbox.className = "checkbox";
    if (activity.completed) checkbox.classList.add("checked");

    checkbox.onclick = async () => {
      try {
        const updated = await updateActivityCompleted(activity.id);

        if (updated.completed) {
          checkbox.classList.add("checked");

          // WALK 체크 ON
          if (activity.activityType === "WALK") {
            showWalkInputs(row, activity);
          }
        } else {
          checkbox.classList.remove("checked");

          // WALK 체크 OFF
          if (activity.activityType === "WALK") {
            removeWalkInputs(row);
          }
        }

        await renderCalendar("calendar-area");
      } catch (e) {
        console.error("Activity 업데이트 실패", e);
      }
    };

    const label = document.createElement("span");
    label.textContent = activity.activityType; // FIXME: 이름이 나오도록 변경

    row.append(checkbox, label);
    section.appendChild(row);

    // 최초 렌더링 시 completed=true WALK 처리
    if (activity.activityType === "WALK" && activity.completed) {
      showWalkInputs(row, activity);
    }
  });

  return section;
}

function showWalkInputs(row, activity) {
  // 이미 있으면 중복 생성 방지
  if (row.nextSibling && row.nextSibling.classList?.contains("walk-extra")) {
    return;
  }

  const wrap = document.createElement("div");
  wrap.className = "walk-extra";

  const steps = document.createElement("input");
  steps.type = "number";
  steps.placeholder = "걸음";

  const hours = document.createElement("input");
  hours.type = "number";
  hours.placeholder = "시간";

  const minutes = document.createElement("input");
  minutes.type = "number";
  minutes.placeholder = "분";

  wrap.append(steps, hours, minutes);

  // blur 시 저장
  [steps, hours, minutes].forEach(() => {
    wrap.addEventListener(
      "blur",
      () => {
        saveWalkDetail(activity.id, {
          totalSteps: Number(steps.value) || 0,
          totalHours: Number(hours.value) || 0,
          totalMinutes: Number(minutes.value) || 0,
        });
      },
      true
    );
  });

  // row 바로 아래 삽입
  row.after(wrap);
}

function removeWalkInputs(row) {
  if (row.nextSibling && row.nextSibling.classList?.contains("walk-extra")) {
    row.nextSibling.remove();
  }
}
