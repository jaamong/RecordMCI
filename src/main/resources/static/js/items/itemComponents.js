export function createSectionTitle(text) {
  const el = document.createElement("div");
  el.className = "section-title";
  el.textContent = text;
  return el;
}

export function createSectionHeader(title, onAdd) {
  const header = document.createElement("div");
  header.className = "section-header";

  const titleEl = document.createElement("strong");
  titleEl.textContent = title;

  if (onAdd) {
    const addBtn = document.createElement("span");
    addBtn.className = "material-symbols-rounded add-btn";
    addBtn.textContent = "add";

    addBtn.onclick = onAdd;
    header.append(titleEl, addBtn);
  } else {
    header.append(titleEl);
  }

  return header;
}

export function createItemRow({ checked, label, onToggle, onEdit, onDelete }) {
  const row = document.createElement("div");
  row.className = "item-row";

  const checkbox = document.createElement("div");
  checkbox.className = "checkbox";
  checkbox.classList.toggle("checked", checked);

  const icon = document.createElement("span");
  icon.className = "material-symbols-rounded checkbox-icon";
  icon.textContent = "check_small";
  checkbox.appendChild(icon);

  checkbox.addEventListener("click", () => {
    const nextChecked = !checkbox.classList.contains("checked");

    // UI 반영
    checkbox.classList.toggle("checked", nextChecked);

    // 외부에 상태 변경 알림 (API 호출 등)
    if (onToggle) {
      onToggle(nextChecked);
    }
  });

  const text = document.createElement("span");
  text.className = "item-label";
  text.textContent = label;

  /* 수정/삭제 아이콘 영역 (hover 시 표시) */
  const actions = document.createElement("div");
  actions.className = "item-actions";

  /* 수정 아이콘 */
  const editBtn = document.createElement("span");
  editBtn.className = "material-symbols-rounded item-action-btn edit-btn";
  editBtn.textContent = "edit";
  editBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    startInlineEdit(row, text, actions, onEdit);
  });

  /* 삭제 아이콘 */
  const deleteBtn = document.createElement("span");
  deleteBtn.className = "material-symbols-rounded item-action-btn delete-btn";
  deleteBtn.textContent = "delete";
  deleteBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    if (onDelete) onDelete();
  });

  actions.append(editBtn, deleteBtn);

  row.append(checkbox, text, actions);
  return row;
}

/* 인라인 수정 모드 시작 */
function startInlineEdit(row, textEl, actionsEl, onEdit) {
  const currentName = textEl.textContent;
  let isFinished = false;

  /* input 생성 */
  const input = document.createElement("input");
  input.type = "text";
  input.className = "item-edit-input";
  input.value = currentName;

  /* 기존 요소 숨기기 */
  textEl.style.display = "none";
  actionsEl.style.display = "none";

  /* input 삽입 */
  row.insertBefore(input, actionsEl);
  input.focus();
  input.select();

  /* UI 복원 */
  const restore = () => {
    if (isFinished) return;
    isFinished = true;
    input.remove();
    textEl.style.display = "";
    actionsEl.style.display = "";
  };

  /* 수정 완료 처리 */
  const finishEdit = async () => {
    if (isFinished) return;
    const newName = input.value.trim();

    if (newName && newName !== currentName && onEdit) {
      await onEdit(newName);
    }

    restore();
  };

  /* Enter 키로 수정 완료 */
  input.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      e.preventDefault();
      finishEdit();
    } else if (e.key === "Escape") {
      restore();
    }
  });

  /* 포커스 잃으면 취소 */
  input.addEventListener("blur", () => {
    restore();
  });
}
