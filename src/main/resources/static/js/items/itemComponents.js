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

export function createItemRow({ checked, label, onToggle }) {
  const row = document.createElement("div");
  row.className = "item-row";

  const checkbox = document.createElement("div");
  checkbox.className = "checkbox";
  checkbox.classList.toggle("checked", checked);
  checkbox.onclick = onToggle;

  const icon = document.createElement("span");
  icon.className = "material-symbols-rounded checkbox-icon";
  icon.textContent = "check_small";
  checkbox.appendChild(icon);

  const text = document.createElement("span");
  text.textContent = label;

  row.append(checkbox, text);
  return row;
}
