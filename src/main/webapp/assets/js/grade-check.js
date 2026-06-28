function checkSignup() {
    const login = document.querySelector("[name='loginName']").value.trim();
    const password = document.querySelector("[name='password']").value;
    const mail = document.querySelector("[name='mailAddress']").value.trim();
    if (!/^[a-zA-Z][a-zA-Z0-9_]{3,15}$/.test(login)) {
        alert("登录名需以字母开头，长度 4-16 位");
        return false;
    }
    if (!/^(?=.*[A-Za-z])(?=.*\d).{6,18}$/.test(password)) {
        alert("密码需为 6-18 位且包含字母和数字");
        return false;
    }
    if (!/^[^@\s]+@[^@\s]+\.[^@\s]+$/.test(mail)) {
        alert("邮箱格式不正确");
        return false;
    }
    return true;
}

function ask(message) {
    return confirm(message);
}

function checkScoreForm() {
    const score = Number(document.querySelector("[name='scoreValue']").value);
    const term = document.querySelector("[name='termLabel']").value.trim();
    if (!/^20\d{2}-(春|秋)$/.test(term)) {
        alert("学期格式示例：2026-春 或 2026-秋");
        return false;
    }
    if (Number.isNaN(score) || score < 0 || score > 100) {
        alert("成绩必须在 0 到 100 之间");
        return false;
    }
    return true;
}

function checkStudentForm() {
    const no = document.querySelector("[name='studentNo']").value.trim();
    const year = Number(document.querySelector("[name='enrollmentYear']").value);
    if (!/^\d{9}$/.test(no)) {
        alert("学号需为 9 位数字");
        return false;
    }
    if (year < 2000 || year > 2100) {
        alert("入学年份不合理");
        return false;
    }
    return true;
}
