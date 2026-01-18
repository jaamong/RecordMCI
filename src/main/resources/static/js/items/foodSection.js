import { createSectionHeader, createItemRow } from "./itemComponents.js";
import { createFoodForm } from "./itemForms.js";
import { updateCalendarDot } from "../calendar.js";

export function createFoodSection(record) {
  const section = document.createElement("div");
  section.className = "item-section";

  section.appendChild(
    createSectionHeader("식품", () => {
      if (section.querySelector(".add-form")) return;

      const form = createFoodForm({
        dailyRecordId: record.id,
        onSuccess: (food) => {
          record.foods.push(food);
          section.appendChild(createFoodRow(food, record));
          updateCalendarDot("food", false);
          form.remove();
        },
        onCancel: () => form.remove(),
      });

      section.appendChild(form);
    })
  );

  record.foods.forEach((food) => {
    section.appendChild(createFoodRow(food, record));
  });

  return section;
}

function createFoodRow(food, record) {
  const row = createItemRow({
    checked: food.consumed,
    label: food.name,
    onToggle: async () => {
      const updated = await updateFoodConsumed(food.id);
      food.consumed = updated.consumed;

      const anyConsumed = record.foods.some((f) => f.consumed);
      updateCalendarDot("food", anyConsumed);
    },
    onEdit: async (newName) => {
      const updated = await updateFoodName(food.id, newName);
      food.name = updated.name;
      row.querySelector(".item-label").textContent = updated.name;
    },
    onDelete: async () => {
      if (confirm(`"${food.name}" 항목을 삭제하시겠습니까?`)) {
        await deleteFood(food.id);
        const idx = record.foods.indexOf(food);
        if (idx > -1) record.foods.splice(idx, 1);
        row.remove();

        const anyConsumed = record.foods.some((f) => f.consumed);
        updateCalendarDot("food", anyConsumed);
      }
    },
  });

  return row;
}
