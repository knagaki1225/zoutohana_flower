// ========== 書評一覧の一括操作 ==========
const statusTextMap = {
  INITIAL: "一次審査未通過",
  FIRST_STAGE_PASSED: "一次審査通過",
  SECOND_STAGE_PASSED: "ノミネート",
  AWARDED: "大賞"
};

document.querySelector('[data-modal-target="change-confirm-modal"]')
  .addEventListener("click", () => {

    const checked = document.querySelectorAll(".review-checkbox:checked");
    const listEl = document.getElementById("selectedReviewList");
    const countEl = document.getElementById("selectedCount");
    const statusSelect = document.getElementById("statusSelect");
    const statusTextEl = document.getElementById("selectedStatusText");

    countEl.textContent = checked.length;
    statusTextEl.textContent = statusTextMap[statusSelect.value];
    listEl.innerHTML = "";

    checked.forEach(cb => {
      const id = cb.value;
      const title = cb.dataset.title;
      const book = cb.dataset.book;

      const li = document.createElement("li");
      li.textContent = `[No.${id}] ${title}（書籍: ${book}）`;
      listEl.appendChild(li);
    });

    if (checked.length === 0) {
      listEl.innerHTML = "<li>※ 書評が選択されていません</li>";
    }
});

document.getElementById("statusSelect").addEventListener("change", e => {
  document.getElementById("selectedStatusText").textContent =
    statusTextMap[e.target.value];
});

const executeBtn = document.getElementById("confirmStatusUpdateBtn");

function toggleExecuteButton() {
  const checked = document.querySelectorAll(".review-checkbox:checked");
  executeBtn.disabled = checked.length === 0;
}

document.querySelectorAll(".review-checkbox")
  .forEach(cb => cb.addEventListener("change", toggleExecuteButton));

executeBtn.addEventListener("click", () => {
  document.getElementById("statusUpdateForm").submit();
});

// ========== 書評一覧の選択 ==========
document.addEventListener("DOMContentLoaded", () => {
  const checkAll = document.getElementById("checkAllReviews");
  const checkboxes = document.querySelectorAll(".review-checkbox");

  if (!checkAll) return;

  checkAll.addEventListener("change", () => {
    checkboxes.forEach(cb => {
      cb.checked = checkAll.checked;
    });

    toggleExecuteButton();
  });

  checkboxes.forEach(cb => {
    cb.addEventListener("change", () => {
      const allChecked = [...checkboxes].every(c => c.checked);
      const noneChecked = [...checkboxes].every(c => !c.checked);

      checkAll.indeterminate = !allChecked && !noneChecked;
      checkAll.checked = allChecked;

      toggleExecuteButton();
    });
  });
});
