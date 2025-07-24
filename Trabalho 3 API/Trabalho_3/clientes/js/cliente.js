// cliente_node.js
const axios = require('axios');

const BASE_URL = "http://192.168.0.102:8080/api";

// Criar colmeia
async function criarColmeia(nomeApicultor, capacidadeAbelhas, capacidadeMel) {
    try {
        const response = await axios.post(`${BASE_URL}/colmeia_new`, null, {
            params: {
                nomeApicultor,
                capacidadeAbelhas,
                capacidadeMel
            }
        });
        return response.data;
    } catch (error) {
        console.error("Erro ao criar colmeia:", error.message);
        if (error.response) {
            console.error("Detalhes:", error.response.data);
        }
    }
}

// Listar colmeias
async function listarColmeias(nomeApicultor) {
    try {
        const response = await axios.get(`${BASE_URL}/colmeia_list`, {
            params: { nomeApicultor }
        });
        return response.data;
    } catch (error) {
        console.error("Erro ao listar colmeias:", error.message);
        if (error.response) {
            console.error("Detalhes:", error.response.data);
        }
    }
}

// Adicionar abelha
async function adicionarAbelha(idColmeia, quantidade, rainhaPresente) {
    try {
        const response = await axios.post(`${BASE_URL}/colmeia_add_abelhas`, null, {
            params: {
                idColmeia,
                quantidade,
                rainhaPresente
            }
        });
        return response.data;
    } catch (error) {
        console.error("Erro ao adicionar abelha:", error.message);
        if (error.response) {
            console.error("Detalhes:", error.response.data);
        }
    }
}

// remover colmeia
async function removerColmeia(idColmeia) {
    try {
        const response = await axios.delete(`${BASE_URL}/colmeia_del/${idColmeia}`);
        return response.data || "Colmeia removida com sucesso";
    } catch (error) {
        return handleError(error, "remover colmeia");
    }
}


// Teste
(async () => {
    console.log("Criando colmeia...");
    const colmeia = await criarColmeia("Guilherme", 1500, 75);
    console.log("Nova colmeia:", colmeia);

    console.log("\nListando colmeias...");
    const colmeias = await listarColmeias("Guilherme");
    console.log("Colmeias de Guilherme:", colmeias);

    console.log("\nAdicionando abelha...");
    const abelha = await adicionarAbelha(1, 10, true);
    console.log("Abelha adicionada:", abelha);

    console.log("\nRemovendo colmeia...");
    const resultadoRemocao = await removerColmeia(1);
    console.log("Resultado da remoção:", resultadoRemocao);

})();