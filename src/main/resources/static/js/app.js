document.addEventListener("DOMContentLoaded", () => {
  const now = new Date();
  currentYear = now.getFullYear();
  currentMonth = now.getMonth() + 1;

  renderCalendar("calendar-area");
});
