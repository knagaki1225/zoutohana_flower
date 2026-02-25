    /* =========================
        初期スクロール制御
    ========================= */
    if ('scrollRestoration' in history) {
        history.scrollRestoration = 'manual';
    }

    /* =========================
        タップ演出制御
    ========================= */
    const tapTarget = document.getElementById('tapTarget');
    const bookText = document.getElementById('bookText');
    const nextStep = document.getElementById('nextStep');

    let isActivated = false;
    
    // tapTargetが存在する場合ときだけ
    if (tapTarget) {
        // トップの画面がクリックされたときの処理
        tapTarget.addEventListener('click', function () {

            // 一度実行したら、二度とアニメーションは実行しない
            // isActivated が true のときは返す
            if (isActivated) return;
            isActivated = true;
            // isActivated をtrueにリセットしたあとにフェイドアウトを実行
            tapTarget.classList.add('fade-out');

            // 3秒後に下にスクロール
            setTimeout(function () {
                // スクロール禁止を解除
                document.body.classList.remove('no-scroll');
                //position: fixed を解除して通常スクロールに戻すclassの設定をしている
                bookText.classList.add('release-fixed');

                // nextStepまで下のエリアへスクロール
                nextStep.scrollIntoView({
                    behavior: 'smooth'
                });

                // 下に移動完了した頃にトップを直す
                setTimeout(function () {
                    // これで「上のimg」がまた表示されます
                    tapTarget.classList.remove('fade-out');
                }, 1000); // 下に移動してから2秒待機
            }, 1000); // フェード開始から3秒待機
        });
    }

    const pathname = window.location.pathname;
    const urlKey = pathname.split('/').pop();

    const VOTE_KEY = 'vote';



// ================共通関数本を押してからモーダルの表示======================
function Allbook(data, book){
        // モーダルのブックマークボタンのidをDOMから取得
        // const voteBtn = document.getElementById('voteBtn');
        // // sample関数に本のIDを入れて呼び出すように設定
        // if(voteBtn){
        //     voteBtn.setAttribute('onclick',`Sample(${data.id})`);
        // }

        console.log(data.icon);

        // 2. モーダル内の各パーツにデータをセット
        // アイコン・名前・属性・プロフィール文
        const iconElem = document.getElementById('modalIcon');
        if (iconElem) iconElem.src = data.icon;

        const nameElem = document.getElementById('modalName');
        if (nameElem) nameElem.innerText = data.name;

        const infoElem = document.getElementById('modalInfo');
        if (infoElem) infoElem.innerText = `${data.age} / ${data.gender} / ${data.address}`;

        const profileTextElem = document.getElementById('modalProfileText');
        if (profileTextElem) profileTextElem.innerText = data.text;

        // 感想テキストエリアを「初期状態」に作り直す
        // これをやらないと、前回の改行処理でIDが消えているため、次のデータが流し込めません
        
const modal = document.getElementById('bookDetailModal');
    modal.style.display = 'flex'; // ★ここで先に表示！
    document.body.classList.add('no-scroll');


    const textBox = document.querySelector('.modal-textFirst-box');

    // ■ 箱の幅に合わせて文字数を計算して表示する関数
    const renderText = () => {
        if (!textBox) return;

        // 1. 今の「テキストボックスの幅」を取得
        const boxWidth = textBox.clientWidth; 

        if (boxWidth === 0) return; // 幅が取れなかったら何もしない

        // 2. 1文字あたりの幅（px）を設定して割り算する
        // 例：文字サイズが16pxくらいなら、余白込みで「18」くらいで割ると丁度いいです
        // ★この「19」という数字をいじると、文字の詰め具合が変わります
        const charSize = 22; 

        // 箱の幅 ÷ 1文字の幅 ＝ 入る文字数
        let lineLength = Math.floor(boxWidth / charSize);

        // ※少なすぎたり多すぎたりしないように制限
        if (lineLength < 10) lineLength = 10; // 最低10文字
        if (lineLength > 50) lineLength = 50; // 最高50文字

        // ★確認用ログ
        console.log(`箱の幅: ${boxWidth}px / 計算した文字数: ${lineLength}文字`);

        // 3. テキスト分割処理
        let textHtml = "";
        const content = data.content; 

        for (let i = 0; i < content.length; i += lineLength) {
            const line = content.substr(i, lineLength);
            textHtml += `<p class="modal-textFirst">${line}</p>`;
        }

        // 4. HTML流し込み
        textBox.innerHTML = `
            <img src="/img/rose.png" class="modal-rose" alt="">
            <h3 id="modalTitle">${data.title}</h3>
            <div class="text-container">
                ${textHtml}
            </div>
        `;
    };

    // ■ 実行
    // 先に display: flex にしたので、ここで正しく幅が取れます
    renderText();

    // ■ 画面サイズ変更時も再計算
    window.removeEventListener('resize', renderText);
    window.addEventListener('resize', renderText);

}

    // モーダル関連変数
    const modal = document.getElementById('bookDetailModal');
    const closeBtn = document.getElementById('closeModal');
    const modalScrollOverlay = document.getElementById('modalScrollOverlay');

    if (closeBtn) {
        closeBtn.addEventListener('click', function () {
            modal.style.display = 'none';
            document.body.classList.remove('no-scroll');
        });
    }

// モーダル外クリックで閉じる
window.addEventListener('click', function (event) {
    if (event.target == modal) {
        modal.style.display = 'none';
        document.body.classList.remove('no-scroll');
    }
});

//  JSONデータを受け取って神棚リストを作成する
async function createShintoShelf(booksData) {
    // 神棚レイアウトの表示箇所をどこにいれるか
    // この神棚レイアウト自体をDOMにいれる
    const shintoContainer = document.querySelector('.book-shinto-box');
    shintoContainer.innerHTML = "";
    if(booksData === null){
        return;
    }
    // 受け取ったデータの数文繰り返す
    // data = 一冊分の本のデータ
    booksData.forEach(data => {
        // 今からやってること↓これの作成
        // <div class="all-book" style="background-image: url('data.image');"></div>

        // 大枠のdivをDOMに入れて
        const setDiv = document.createElement('div');
        // クラスをつける
        setDiv.classList.add('book-shinto-set');
        // 本の本体のDOMに入れて
        const allBook = document.createElement('div');
        // クラスをつける
        allBook.classList.add('all-book');
        // 画像をセット
        // background-image: url('data.image');
        allBook.style.backgroundImage = `url('${data.image}')`;

        // 共通関数本を押してからモーダルの表示
        allBook.addEventListener('click', function() {
            // これallBookを入れる理由が
            // ・押された本がどれかの記録
            // それをすることで既読処理とタグの処理ができる
            Allbook(data, allBook);
        });

        // 今からやってること↓これの作成
        // <div class="book-text-overlay">タイトル</div>

        // 本の上のテキスト (.book-text-overlay)
        const overlay = document.createElement('div');
        overlay.classList.add('book-text-overlay');
        // JSONの title を入れる
        overlay.innerHTML = data.title;
        // 本の中にテキストを入れる
        // これは別々の
        // <div class="all-book"></div><div class="book-text-overlay">タイトル</div>
        // この二つをallBookの中にタイトルが入るテキストを入れている
        allBook.appendChild(overlay);

        // 神棚の台座
        const altarDiv = document.createElement('div');
        altarDiv.classList.add('thema-shinto-altar');

        // 投稿者名
        const postedBy = document.createElement('p');
        postedBy.classList.add('posted-by');
        // 投稿者をもってきて入れている
        postedBy.innerText = `投稿者：${data.name}`;

        // 投票数エリア
        const voteBox = document.createElement('div');
        voteBox.classList.add('vote-box');

        let voteCount;
        if(data.voteCount !== null){
            voteCount = data.vote;
            // 受け取った投票数をpタグで表示し投票数エリアに入れている
        }else{
            voteCount = "-";
        }
        voteBox.innerHTML = `<p class="vote-text">${voteCount}<span class="vote">票</span></p>`;

        // 全部セットして親箱に追加
        setDiv.appendChild(allBook);
        setDiv.appendChild(altarDiv);
        setDiv.appendChild(postedBy);
        setDiv.appendChild(voteBox);

        shintoContainer.appendChild(setDiv);
    });
}


    /* =========================
        モーダル内のテキストを20文字ごとに改行して表示する
    ========================= */
    document.addEventListener("DOMContentLoaded", () => {

        splitTextToParagraphs("textFirst", "modal-textFirst", 18);
        splitTextToParagraphs("textSecond", "modal-textSecond", 18);

    });

    function splitTextToParagraphs(textId, className, maxLength) {
        const originalP = document.getElementById(textId);
        if (!originalP) return;

        const container = originalP.parentElement;
        const text = originalP.textContent.trim();
        const title = container.querySelector("h3");
        const img = container.querySelector("img");

        const lines = [];
        for (let i = 0; i < text.length; i += maxLength) {
            lines.push(text.slice(i, i + maxLength));
        }

        // 元のp削除
        container.innerHTML = "";

        if (img) container.appendChild(img);
        if (title) container.appendChild(title);

        // 新しいpを追加
        lines.forEach(line => {
            const p = document.createElement("p");
            p.className = className;
            p.textContent = line;
            container.appendChild(p);
        });
    }

    

    // 自分が投票している作品の表示
function oneShintobook(data) {
    const oneShintoContainer = document.querySelector('.one-book-shinto-box');

    // 大枠 (class="one-book-shinto-set")
    const setDiv = document.createElement('div');
    setDiv.classList.add('one-book-shinto-set');

    // 本の本体 (class="one-book")
    const oneBook = document.createElement('div');
    oneBook.classList.add('one-book');
    oneBook.style.backgroundImage = `url('${data.image}')`;

    oneBook.addEventListener('click', function() {
        Allbook(data, oneBook);
    });

    // 本の上の文字 (class="one-book-text-overlay")
    const overlay = document.createElement('div');
    overlay.classList.add('one-book-text-overlay');
    overlay.innerHTML = data.title;
    oneBook.appendChild(overlay); // 本の中に文字を入れる

    const penBox = document.createElement('div');
    penBox.classList.add('pen-box');

    // ペン画像
    const penImg = document.createElement('img');
    penImg.src = '../img/pen.png'; // 画像のパス
    penImg.classList.add('pen');
    penImg.alt = '';

    // 投稿者のテキスト
    const penText = document.createElement('p');
    penText.classList.add('pen-text');
    penText.innerText = `投稿者：${data.name}`; // データから名前を入れる

    // pen-box の中に画像とテキストを入れる
    penBox.appendChild(penImg);
    penBox.appendChild(penText);
    setDiv.appendChild(oneBook);
    setDiv.appendChild(penBox);

    // 画面の箱に追加して表示完了
    oneShintoContainer.appendChild(setDiv);
}


/* =========================================================
    ページ読み込み完了時の処理
   ========================================================= */
window.onload = async function () {
    if ('scrollRestoration' in history) {
        history.scrollRestoration = 'manual';
    }
    window.scrollTo(0, 0);

    const nominateBooksResponse = await fetch(`/api/reviews/nominate/${urlKey}`);
    const nominateBooks = await nominateBooksResponse.json();
    console.log(nominateBooks);
    await createShintoShelf(nominateBooks);

    // その他の初期化

    const currentData = localStorage.getItem(VOTE_KEY);

    const voteIds = currentData ? JSON.parse(currentData) : [];

    if (voteIds.length !== 0){
        const voteReviewResponse = await fetch(`/api/reviews/voted/${urlKey}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            // 配列をそのままJSON化してBodyに詰める
            body: JSON.stringify(voteIds)
        });

        if(voteReviewResponse.status === 200){
            const voteReview = await voteReviewResponse.json();
            console.log(voteReview);
            oneShintobook(voteReview);
        }else{
            const voteReview = document.getElementById('voteReview');
            voteReview.style.display = 'none';
        }

    }else{
        const voteReview = document.getElementById('voteReview');
        voteReview.style.display = 'none';
    }

};
/* =========================================================
                            販売拠点
   ========================================================= */

// DOMContentLoadedこれは画像などが読み込まれる前に動かないようにしている
document.addEventListener('DOMContentLoaded', () => {
    // ここでfilter-btnクラスがついているボタン三つを入れている
    const filterBtns = document.querySelectorAll('.filter-btn');
    // これはshop-card.area-titleクラスがついている要素を入れている
    const filterItems = document.querySelectorAll('.shop-card, .area-title');

    // すべて 岩手 青森すべてのボタンを見るまで繰り返す
    filterBtns.forEach(btn => {
        // その岩手ボタンがクリックされたら実行する
        btn.addEventListener('click', () => {
            // 今ついてるactiveにしているボタンをいったん全部みて外す
            filterBtns.forEach(b => b.classList.remove('active'));
            // クリックされたボタンにactiveをつける
            btn.classList.add('active');

            // htmlに書いてあるそれぞれall iwate aomoriのボタンをおしてそれを読み取りどれを表示させるか
            const filterValue = btn.getAttribute('data-filter');

            // すべてのアイテムを確認して表示・非表示を切り替え
            filterItems.forEach(item => {
                // そのアイテムが持っているエリア情報を取得
                const itemArea = item.getAttribute('data-area');
                // ここでさっき取得したどれを表示させるかのfilterValueをif文で判定
                if (filterValue === 'all') {
                    // 「すべて」なら全部表示（hideクラスを外す）
                    item.classList.remove('hide');
                } else {
                    // 選択されたエリアと一致するか？
                    if (filterValue === itemArea) {
                        item.classList.remove('hide'); // 表示
                    } else {
                        item.classList.add('hide');    // 非表示
                    }
                }
            });
        });
    });
});