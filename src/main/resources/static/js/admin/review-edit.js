// ========== 書籍ジャンル複数選択 ==========
document.addEventListener("DOMContentLoaded", () => {
  const options = window.optionsList || [];
  const input = document.getElementById("genre-input");
  const tagsEl = document.getElementById("multi-select-tags");
  const optionsEl = document.getElementById("multi-select-options");
  const hiddenArea = document.getElementById("genre-hidden-area");
  const container = document.getElementById("multi-select-wrap");

  if (!input || !tagsEl || !optionsEl || !hiddenArea) return;

  let selected = window.initialGenres || [];

  function renderTags() {
    tagsEl.innerHTML = "";
    hiddenArea.innerHTML = "";

    selected.forEach((g, index) => {
      const tag = document.createElement("div");
      tag.className = "multi-select-tag icon-center";
      tag.innerHTML = `
        ${g.name}
        <button data-index="${index}">
          <span class="material-symbols-outlined mt-1">cancel</span>
        </button>
      `;
      tagsEl.appendChild(tag);

      const hidden = document.createElement("input");
      hidden.type = "hidden";
      hidden.name = "genres";
      hidden.value = g.id;
      hiddenArea.appendChild(hidden);
    });
  }

  function renderOptions(keyword) {
    optionsEl.innerHTML = "";

    const filtered = options
      .filter(g =>
        !keyword ||
        (g.name || "").includes(keyword) ||
        (g.furigana || "").includes(keyword)
      )
      .filter(g => !selected.some(s => s.id === g.id));

    filtered.forEach(g => {
      const div = document.createElement("div");
      div.className = "multi-select-option";
      div.textContent = g.name;
      div.onclick = () => {
        selected.push(g);
        renderTags();
        input.value = "";
        optionsEl.hidden = true;
      };
      optionsEl.appendChild(div);
    });

    optionsEl.hidden = filtered.length === 0;
  }

  input.addEventListener("input", () => {
    renderOptions(input.value.trim());
  });

  input.addEventListener("focus", () => {
    renderOptions(input.value.trim());
  });

  // タグ削除
  tagsEl.addEventListener("click", (e) => {
    const btn = e.target.closest("button");
    if (!btn) return;
    selected.splice(btn.dataset.index, 1);
    renderTags();
  });

  container.addEventListener("click", () => {
    input.focus();
    renderOptions("");
  });

  input.addEventListener("blur", () => {
    setTimeout(() => optionsEl.hidden = true, 200);
  });

  renderTags();
});
