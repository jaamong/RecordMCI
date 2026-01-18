/* Toast 알림 표시 */
function showToast(message) {
  const toast = document.createElement("div");
  toast.className = "toast";
  toast.textContent = message;

  document.body.appendChild(toast);

  /* 1초 후 fade-out 시작 */
  setTimeout(() => {
    toast.classList.add("fade-out");
  }, 1000);

  /* fade-out 완료 후 제거 */
  toast.addEventListener("animationend", () => {
    toast.remove();
  });
}
