// ========== 書評印刷のやり方開閉 ==========
document.addEventListener('DOMContentLoaded', () => {
  const title = document.querySelector('.toggle-title');
  const body = document.querySelector('.toggle-body');
  const text = document.querySelector('.toggle-text');

  if (!title || !body || !text) return;

  title.addEventListener('click', () => {
    body.classList.toggle('is-open');

    const isOpen = body.classList.contains('is-open');
    text.textContent = isOpen
      ? '(クリックして閉じる)'
      : '(クリックして開く)';
  });
});

// ========== すべて選択のチェックボックス ==========
document.getElementById("checkAll").addEventListener("change", function () {
  const checked = this.checked;
  document.querySelectorAll(".print-check").forEach(cb => {
    cb.checked = checked;
  });
});

document.querySelectorAll(".print-check").forEach(cb => {
  cb.addEventListener("change", () => {
    const all = document.querySelectorAll(".print-check");
    const checked = document.querySelectorAll(".print-check:checked");

    document.getElementById("checkAll").checked = (all.length === checked.length);
  });
});


// ========== チェックを入れた項目をコピー ==========
document.getElementById("copyBtn").addEventListener("click", async () => {
  let result = "";

  document.querySelectorAll(".print-check:checked").forEach(cb => {
    const label = cb.dataset.label;
    const valueTd = cb.closest("tr").querySelector(".print-value");
    const value = valueTd ? valueTd.innerText.trim() : "";
    result += `${label} : ${value}\n`;
  });

  const content = document.querySelector("textarea");
  if (content) {
    result += "書評本文 : " + content.value;
  }

  try {
    await navigator.clipboard.writeText(result);
    alert("クリップボードにコピーしました");
  } catch (e) {
    console.error(e);
    alert("コピーに失敗しました（HTTPS環境か確認してください）");
  }
});
