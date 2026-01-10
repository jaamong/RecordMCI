import { createSectionHeader, createItemRow } from "./itemComponents.js";
import { createActivityForm } from "./itemForms.js";
import { updateCalendarDot } from "../calendar.js";

/* walk 입력 UI 생성 (기존 로직 복원) */
function createWalkInputs(activity) {
  const wrapper = document.createElement("div");
  wrapper.className = "walk-inputs";

  const steps = document.createElement("input");
  steps.type = "number";
  steps.placeholder = "총 걸음수";
  steps.value = activity.totalSteps ?? "";

  const hours = document.createElement("input");
  hours.type = "number";
  hours.placeholder = "시";
  hours.value = activity.totalHours ?? "";

  const minutes = document.createElement("input");
  minutes.type = "number";
  minutes.placeholder = "분";
  minutes.value = activity.totalMinutes ?? "";

  const saveBtn = document.createElement("button");
  saveBtn.textContent = "저장";

  saveBtn.onclick = async () => {
    await saveWalkDetail(activity.id, {
      step: Number(steps.value),
      hour: Number(hours.value),
      minutes: Number(minutes.value),
    });
  };

  wrapper.append(steps, hours, minutes, saveBtn);
  return wrapper;
}

export function createActivitySection(record) {
  const section = document.createElement("div");
  section.className = "item-section";

  section.appendChild(
    createSectionHeader("활동", () => {
      if (section.querySelector(".add-form")) return;

      const form = createActivityForm({
        dailyRecordId: record.id,
        onSuccess: (activity) => {
          record.activities.push(activity);
          section.appendChild(createActivityRow(activity));
          updateCalendarDot("activity", false);
          form.remove();
        },
        onCancel: () => form.remove(),
      });

      section.appendChild(form);
    })
  );

  record.activities.forEach((activity) => {
    section.appendChild(createActivityRow(activity));
  });

  return section;
}

function createActivityRow(activity) {
  const wrapper = document.createElement("div");
  wrapper.className = "activity-wrapper";

  const row = createItemRow({
    checked: activity.completed,
    label: activity.name,
    onToggle: async (nextChecked) => {
      const updated = await updateActivityCompleted(activity.id);
      activity.completed = updated.completed;

      updateCalendarDot("activity", updated.completed);

      // walk 전용 분기
      if (isWalkActivity(activity)) {
        toggleWalkInputs(wrapper, activity);
      }
    },
  });

  wrapper.appendChild(row);

  // 이미 완료된 walk는 최초 렌더 시에도 표시
  if (activity.completed && isWalkActivity(activity)) {
    wrapper.appendChild(createWalkInputs(activity));
  }

  return wrapper;
}

function isWalkActivity(activity) {
  return activity.walk === true || activity.name === "걷기";
}

function toggleWalkInputs(wrapper, activity) {
  const existing = wrapper.querySelector(".walk-inputs");

  if (activity.completed) {
    if (!existing) {
      wrapper.appendChild(createWalkInputs(activity));
    }
  } else {
    if (existing) {
      existing.remove();
    }
  }
}
