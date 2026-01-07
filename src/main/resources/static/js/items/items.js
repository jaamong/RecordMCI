import { createSectionTitle } from "./itemComponents.js";
import { createFoodSection } from "./foodSection.js";
import { createActivitySection } from "./activitySection.js";
import { createMemoSection } from "./memoSection.js";

export function renderItems(record, dateText) {
  const container = document.getElementById("items-area");
  container.innerHTML = "";

  if (!record) {
    container.textContent = "데이터 없음";
    return;
  }

  container.appendChild(createSectionTitle(dateText));
  container.appendChild(createFoodSection(record));
  container.appendChild(createActivitySection(record));
  container.appendChild(createMemoSection(record));
}
