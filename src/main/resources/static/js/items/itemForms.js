export function createFoodForm({ dailyRecordId, onSuccess, onCancel }) {
  const form = document.createElement("div");
  form.className = "add-form";

  const input = document.createElement("input");
  input.placeholder = "음식 이름";

  const select = document.createElement("select");
  [
    { label: "탄수화물", value: "CARBO" },
    { label: "채소", value: "VEGETABLE" },
    { label: "과일", value: "FRUIT" },
    { label: "단백질", value: "PROTEIN" },
    { label: "지방", value: "FAT" },
  ].forEach(({ label, value }) => {
    const opt = document.createElement("option");
    opt.value = value;
    opt.textContent = label;
    select.appendChild(opt);
  });

  const save = document.createElement("button");
  save.textContent = "추가";

  const cancel = document.createElement("button");
  cancel.textContent = "취소";

  save.onclick = async () => {
    const name = input.value.trim();
    if (!name) return alert("음식 이름 입력");

    const food = await createFood({
      dailyRecordId,
      name,
      nutrientType: select.value,
    });
    onSuccess(food);
  };

  cancel.onclick = onCancel;

  form.append(input, select, save, cancel);
  return form;
}

export function createActivityForm({ dailyRecordId, onSuccess, onCancel }) {
  const form = document.createElement("div");
  form.className = "add-form";

  const input = document.createElement("input");
  input.placeholder = "활동 이름";

  const save = document.createElement("button");
  save.textContent = "추가";

  const cancel = document.createElement("button");
  cancel.textContent = "취소";

  save.onclick = async () => {
    const name = input.value.trim();
    if (!name) return alert("활동 이름 입력");

    const activity = await createActivity({ dailyRecordId, name });
    onSuccess(activity);
  };

  cancel.onclick = onCancel;

  form.append(input, save, cancel);
  return form;
}
