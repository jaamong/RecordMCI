import { createSectionHeader, createItemRow } from "./itemComponents.js";
import { createActivityForm } from "./itemForms.js";
import { updateCalendarDot } from "../calendar.js";

/* walk 입력 UI 생성 */
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

  /* 기존 데이터 유무에 따라 버튼 텍스트 결정 */
  const hasData = activity.totalSteps || activity.totalHours || activity.totalMinutes;
  const saveBtn = document.createElement("button");
  saveBtn.textContent = hasData ? "수정" : "저장";

  saveBtn.onclick = async () => {
    await saveWalkDetail(activity.id, {
      totalSteps: Number(steps.value),
      totalHours: Number(hours.value),
      totalMinutes: Number(minutes.value),
    });

    /* 저장 성공 시 Toast 표시 및 버튼 텍스트 변경 */
    showToast("저장되었습니다");
    saveBtn.textContent = "수정";
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
          section.appendChild(createActivityRow(activity, record));
          updateCalendarDot("activity", false);
          form.remove();
        },
        onCancel: () => form.remove(),
      });

      section.appendChild(form);
    })
  );

  record.activities.forEach((activity) => {
    section.appendChild(createActivityRow(activity, record));
  });

  return section;
}

function createActivityRow(activity, record) {
  const wrapper = document.createElement("div");
  wrapper.className = "activity-wrapper";

  const row = createItemRow({
    checked: activity.completed,
    label: activity.name,
    onToggle: async (nextChecked) => {
      const updated = await updateActivityCompleted(activity.id);
      activity.completed = updated.completed;

      const anyCompleted = record.activities.some((a) => a.completed);
      updateCalendarDot("activity", anyCompleted);

      // walk 전용 분기
      if (isWalkActivity(activity)) {
        toggleWalkInputs(wrapper, activity);
      }
    },
    onEdit: async (newName) => {
      const updated = await updateActivityName(activity.id, newName);
      activity.name = updated.name;
      row.querySelector(".item-label").textContent = updated.name;
    },
    onDelete: async () => {
      if (confirm(`"${activity.name}" 항목을 삭제하시겠습니까?`)) {
        await deleteActivity(activity.id);
        const idx = record.activities.indexOf(activity);
        if (idx > -1) record.activities.splice(idx, 1);
        wrapper.remove();

        const anyCompleted = record.activities.some((a) => a.completed);
        updateCalendarDot("activity", anyCompleted);
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
