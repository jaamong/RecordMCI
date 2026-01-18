import { createSectionHeader } from "./itemComponents.js";
import { updateCalendarDot } from "../calendar.js";

export function createMemoSection(record) {
  const section = document.createElement("div");
  section.className = "item-section memo-section";

  section.appendChild(createSectionHeader("메모"));

  // textarea를 감싸는 wrapper
  const textareaWrapper = document.createElement("div");
  textareaWrapper.className = "memo-textarea-wrapper";

  const textarea = document.createElement("textarea");
  textarea.className = "memo-textarea";
  textarea.maxLength = 1000;
  textarea.value = record.memo ?? "";

  // 글자수 카운터
  const charCounter = document.createElement("span");
  charCounter.className = "memo-char-counter";
  charCounter.textContent = `${textarea.value.length}/1000`;

  textarea.addEventListener("input", () => {
    charCounter.textContent = `${textarea.value.length}/1000`;
  });

  textareaWrapper.append(charCounter, textarea);

  /* 기존 데이터 유무에 따라 버튼 텍스트 결정 */
  const hasData = record.memo && record.memo.trim().length > 0;
  const save = document.createElement("button");
  save.textContent = hasData ? "수정" : "저장";

  save.onclick = async () => {
    const value = textarea.value.trim();
    const saved = await updateMemo(record.id, value);
    textarea.value = saved;
    updateCalendarDot("memo", value.length > 0);

    /* 저장 성공 시 Toast 표시 및 버튼 텍스트 변경 */
    showToast("저장되었습니다");
    save.textContent = "수정";
  };

  section.append(textareaWrapper, save);
  return section;
}
