const newApiUrl = 'http://localhost:8080/api/projects/new';
const detailPageUrl = 'http://127.0.0.1:5500/admin/project-detail.html';

// ==========企画URLのプレビュー==========
const urlKeyInput = document.getElementById('urlKey-input');
const urlKeyPreview = document.getElementById('urlKey-preview');

function updateUrlKeyPreview() {
  if(urlKeyInput.value === ''){
    urlKeyPreview.textContent = '_____'
  } else {
    urlKeyPreview.textContent = urlKeyInput.value
  }
}

if (urlKeyInput) {
  urlKeyInput.addEventListener('input', updateUrlKeyPreview);
  updateUrlKeyPreview();
}

// ==========ロゴ画像のプレビュー==========
const mainImgInput = document.getElementById('mainImg-input');
const mainImgPreview = document.getElementById('mainImg-preview');

if(mainImgInput) {
  mainImgInput.addEventListener('change', (event) => {
    const file = event.target.files[0]

    if (!file) {
      return
    }

    const reader = new FileReader()
    reader.onload = (event) => {
      mainImgPreview.src = event.target.result
      mainImgPreview.style.display = 'block';
    }
    reader.readAsDataURL(file)
  })
}