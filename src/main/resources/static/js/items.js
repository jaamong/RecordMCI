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
  if (record.memo !== undefined) {
    container.appendChild(createMemoSection(record));
  }
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

    const label = document.createElement("span");
    label.textContent = activity.activityType;

    row.append(checkbox, label);
    section.appendChild(row);

    // WALK 상세 입력 영역
    let walkInputs = null;

    if (activity.activityType === "WALK" && activity.completed) {
      walkInputs = createWalkInputs(activity);
      section.appendChild(walkInputs);
    }

    checkbox.onclick = async () => {
      try {
        const updated = await updateActivityCompleted(activity.id);

        checkbox.classList.toggle("checked", updated.completed);

        // WALK true → 입력폼 생성
        if (activity.activityType === "WALK") {
          if (updated.completed) {
            if (!walkInputs) {
              walkInputs = createWalkInputs(updated);
              section.appendChild(walkInputs);
            }
          } else {
            if (walkInputs) {
              walkInputs.remove();
              walkInputs = null;
            }
          }
        }

        await renderCalendar("calendar-area");
      } catch (e) {
        console.error("Activity 업데이트 실패", e);
      }
    };
  });

  return section;
}

function createWalkInputs(activity) {
  const container = document.createElement("div");
  container.className = "walk-inputs";
  container.style.marginLeft = "24px";
  container.style.marginTop = "6px";

  const stepsInput = document.createElement("input");
  stepsInput.type = "number";
  stepsInput.value = activity.totalSteps ?? "";
  stepsInput.placeholder = "걸음 수";

  const hoursInput = document.createElement("input");
  hoursInput.type = "number";
  hoursInput.value = activity.totalHours ?? "";
  hoursInput.placeholder = "시간";

  const minutesInput = document.createElement("input");
  minutesInput.type = "number";
  minutesInput.value = activity.totalMinutes ?? "";
  minutesInput.placeholder = "분";

  const saveBtn = document.createElement("button");
  saveBtn.textContent = "저장";
  saveBtn.style.marginLeft = "8px";

  saveBtn.onclick = async () => {
    const payload = {
      totalSteps: Number(stepsInput.value) || 0,
      totalHours: Number(hoursInput.value) || 0,
      totalMinutes: Number(minutesInput.value) || 0,
    };

    try {
      saveBtn.disabled = true;
      saveBtn.textContent = "저장 중...";

      await updateWalkDetail(activity.id, payload);

      saveBtn.textContent = "저장됨";
      setTimeout(() => (saveBtn.textContent = "저장"), 1000);
    } catch (e) {
      console.error(e);
      alert("WALK 상세 저장 실패");
      saveBtn.textContent = "저장";
    } finally {
      saveBtn.disabled = false;
    }
  };

  container.append(
    labeled("걸음 수", stepsInput),
    labeled("시간", hoursInput),
    labeled("분", minutesInput),
    saveBtn
  );

  return container;
}

function showWalkInputs(row, activity) {
  // 중복 생성 방지
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

  // WALK 상세 값 복원
  if (activity.totalSteps !== undefined) {
    steps.value = activity.totalSteps;
  }
  if (activity.totalHours !== undefined) {
    hours.value = activity.totalHours;
  }
  if (activity.totalMinutes !== undefined) {
    minutes.value = activity.totalMinutes;
  }

  wrap.append(steps, hours, minutes);
  row.after(wrap);

  // blur 시 저장
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
}

function removeWalkInputs(row) {
  if (row.nextSibling && row.nextSibling.classList?.contains("walk-extra")) {
    row.nextSibling.remove();
  }
}

async function updateWalkDetail(activityId, payload) {
  const res = await fetch(
    `http://localhost:8080/api/activities/${activityId}/walk-detail`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    }
  );

  if (!res.ok) {
    throw new Error("WALK 상세 업데이트 실패");
  }

  const json = await res.json();
  return json.data;
}

function labeled(text, input) {
  const wrapper = document.createElement("div");
  wrapper.style.display = "inline-block";
  wrapper.style.marginRight = "8px";

  const label = document.createElement("label");
  label.textContent = text;
  label.style.marginRight = "4px";

  wrapper.append(label, input);
  return wrapper;
}

function createMemoSection(record) {
  const section = document.createElement("div");
  section.style.marginTop = "20px";

  const header = document.createElement("div");
  header.innerHTML = `<strong>Memo</strong>`;

  const textarea = document.createElement("textarea");
  textarea.style.width = "100%";
  textarea.style.height = "80px";
  textarea.maxLength = 1000;
  textarea.value = record.memo ?? "";

  const saveBtn = document.createElement("button");
  saveBtn.textContent = "저장";
  saveBtn.style.marginTop = "6px";

  saveBtn.onclick = async () => {
    const value = textarea.value;

    if (value.length > 1000) {
      alert("메모는 최대 1000자까지 입력할 수 있습니다.");
      return;
    }

    try {
      saveBtn.disabled = true;
      saveBtn.textContent = "저장 중...";

      const savedMemo = await updateMemo(record.id, value);
      textarea.value = savedMemo;

      saveBtn.textContent = "저장됨";
      setTimeout(() => (saveBtn.textContent = "저장"), 1000);

      // 월별 캘린더 dot 즉시 반영
      await renderCalendar("calendar-area");
    } catch (e) {
      console.error(e);
      alert("Memo 저장 실패");
      saveBtn.textContent = "저장";
    } finally {
      saveBtn.disabled = false;
    }
  };

  section.append(header, textarea, saveBtn);
  return section;
}

async function updateMemo(dailyRecordId, memo) {
  const res = await fetch(
    `http://localhost:8080/api/daily-records/${dailyRecordId}/memo`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ memo }),
    }
  );

  if (!res.ok) {
    throw new Error("Memo 저장 실패");
  }

  const json = await res.json();
  return json.data; // 저장된 memo 문자열
}
