const notificationContent = document.getElementById('notification-content');
const targets = [
    "{project-name}",
    "{project-submissionStartAt}",
    "{project-submissionEndAt}",
    "{project-votingStartAt}",
    "{project-votingEndAt}",
    "{user-nickname}",
    "{review-title}",
    "{book-title}"];

const regex = new RegExp(targets.join('|'), 'g');

notificationContent.innerHTML = notificationContent.innerHTML.replace(regex, (match) => {
    return `<span class="merge-tag-preview">${match}</span>`;
});