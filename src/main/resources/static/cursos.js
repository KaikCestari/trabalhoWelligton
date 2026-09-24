const form = document.querySelector("#cursoForm");
const tabela = document.querySelector("#cursosTabela");
const listaVazia = document.querySelector("#listaVazia");
const totalCursos = document.querySelector("#totalCursos");
const mensagem = document.querySelector("#formMensagem");
const formTitle = document.querySelector("#formTitle");

async function verificarSessao() {
    const resposta = await fetch("/api/auth/me");
    if (!resposta.ok) {
        window.location.href = "index.html";
    }
}

async function carregarCursos() {
    const resposta = await fetch("/api/cursos");
    if (resposta.status === 401) {
        window.location.href = "index.html";
        return;
    }

    const cursos = await resposta.json();
    totalCursos.textContent = cursos.length;
    listaVazia.style.display = cursos.length ? "none" : "block";
    tabela.innerHTML = "";

    cursos.forEach((curso) => {
        const linha = document.createElement("tr");
        linha.innerHTML = `
            <td>${curso.nome}</td>
            <td>${curso.categoria}</td>
            <td>${curso.cargaHoraria}h</td>
            <td>${curso.professor}</td>
            <td><span class="${curso.ativo ? "status active" : "status inactive"}">${curso.ativo ? "Ativo" : "Inativo"}</span></td>
            <td class="actions">
                <button type="button" data-action="edit" data-id="${curso.id}">Editar</button>
                <button type="button" data-action="delete" data-id="${curso.id}">Excluir</button>
            </td>
        `;
        tabela.appendChild(linha);
    });
}

function dadosDoFormulario() {
    return {
        nome: document.querySelector("#nome").value.trim(),
        categoria: document.querySelector("#categoria").value.trim(),
        cargaHoraria: Number(document.querySelector("#cargaHoraria").value),
        professor: document.querySelector("#professor").value.trim(),
        ativo: document.querySelector("#ativo").checked
    };
}

function preencherFormulario(curso) {
    document.querySelector("#cursoId").value = curso.id;
    document.querySelector("#nome").value = curso.nome;
    document.querySelector("#categoria").value = curso.categoria;
    document.querySelector("#cargaHoraria").value = curso.cargaHoraria;
    document.querySelector("#professor").value = curso.professor;
    document.querySelector("#ativo").checked = curso.ativo;
    formTitle.textContent = "Editar curso";
    mensagem.textContent = "";
}

function limparFormulario() {
    form.reset();
    document.querySelector("#cursoId").value = "";
    document.querySelector("#ativo").checked = true;
    formTitle.textContent = "Novo curso";
    mensagem.textContent = "";
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const id = document.querySelector("#cursoId").value;
    const resposta = await fetch(id ? `/api/cursos/${id}` : "/api/cursos", {
        method: id ? "PUT" : "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(dadosDoFormulario())
    });

    if (resposta.ok) {
        mensagem.textContent = id ? "Curso atualizado." : "Curso cadastrado.";
        limparFormulario();
        await carregarCursos();
        return;
    }

    mensagem.textContent = "Confira os dados informados.";
});

tabela.addEventListener("click", async (event) => {
    const button = event.target.closest("button");
    if (!button) {
        return;
    }

    const id = button.dataset.id;

    if (button.dataset.action === "edit") {
        const resposta = await fetch(`/api/cursos/${id}`);
        if (resposta.ok) {
            preencherFormulario(await resposta.json());
        }
    }

    if (button.dataset.action === "delete") {
        await fetch(`/api/cursos/${id}`, {
            method: "DELETE"
        });
        limparFormulario();
        await carregarCursos();
    }
});

document.querySelector("#cancelarButton").addEventListener("click", limparFormulario);

document.querySelector("#logoutButton").addEventListener("click", async () => {
    await fetch("/api/auth/logout", {
        method: "POST"
    });
    window.location.href = "index.html";
});

verificarSessao().then(carregarCursos);
