import { createSectionHeader } from "./itemComponents.js";
import { updateCalendarDot } from "../calendar.js";

export function createMemoSection(record) {
  const section = document.createElement("div");
  section.className = "item-section memo-section";

  section.appendChild(createSectionHeader("메모"));

  const textarea = document.createElement("textarea");
  textarea.className = "memo-textarea";
  textarea.value = record.memo ?? "";

  const save = document.createElement("button");
  save.textContent = "저장";

  save.onclick = async () => {
    const value = textarea.value.trim();
    const saved = await updateMemo(record.id, value);
    textarea.value = saved;
    updateCalendarDot("memo", value.length > 0);
  };

  section.append(textarea, save);
  return section;
}
