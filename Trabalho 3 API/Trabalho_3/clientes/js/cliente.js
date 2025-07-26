const axios = require('axios');
const readline = require('readline');

const BASE_URL = "http://172.25.210.208:8080/api";

// Configuração da interface de leitura
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

// Função para exibir o menu
function mostrarMenu() {
  console.log(`
  === MENU API DE APICULTURA ===
  1. Criar nova colmeia
  2. Listar colmeias
  3. Adicionar abelhas a uma colmeia
  4. Remover colmeia
  5. Sair
  `);
}

// Função principal
async function main() {
  let continuar = true;
  
  while (continuar) {
    mostrarMenu();
    
    const opcao = await perguntar("Digite uma opção: ");
    
    switch(opcao) {
      case '1':
        await criarColmeiaMenu();
        break;
      case '2':
        await listarColmeiasMenu();
        break;
      case '3':
        await adicionarAbelhasMenu();
        break;
      case '4':
        await removerColmeiaMenu();
        break;
      case '5':
        continuar = false;
        break;
      default:
        console.log("Opção inválida!");
    }
  }
  
  rl.close();
}

// Funções auxiliares para entrada
function perguntar(pergunta) {
  return new Promise(resolve => {
    rl.question(pergunta, resolve);
  });
}

// Menus específicos
async function criarColmeiaMenu() {
  console.log("\n--- Criar Nova Colmeia ---");
  const nome = await perguntar("Nome do apicultor: ");
  const abelhas = await perguntar("Capacidade de abelhas: ");
  const mel = await perguntar("Capacidade de mel: ");
  
  const colmeia = await criarColmeia(nome, parseInt(abelhas), parseInt(mel));
  console.log("Colmeia criada:", colmeia);
}

async function listarColmeiasMenu() {
  console.log("\n--- Listar Colmeias ---");
  const nome = await perguntar("Nome do apicultor: ");
  const colmeias = await listarColmeias(nome);
  console.log("Colmeias:", colmeias);
}

async function adicionarAbelhasMenu() {
  console.log("\n--- Adicionar Abelhas ---");
  const id = await perguntar("ID da colmeia: ");
  const quantidade = await perguntar("Quantidade de abelhas: ");
  const rainha = await perguntar("Tem rainha? (s/n): ");
  
  const resultado = await adicionarAbelha(
    parseInt(id),
    parseInt(quantidade),
    rainha.toLowerCase() === 's'
  );
  console.log("Resultado:", resultado);
}

async function removerColmeiaMenu() {
  console.log("\n--- Remover Colmeia ---");
  const id = await perguntar("ID da colmeia a remover: ");
  const resultado = await removerColmeia(parseInt(id));
  console.log("Resultado:", resultado);
}

// Suas funções originais (mantidas sem alteração)
async function criarColmeia(nomeApicultor, capacidadeAbelhas, capacidadeMel) {
  try {
    const response = await axios.post(`${BASE_URL}/colmeia_new`, null, {
      params: { nomeApicultor, capacidadeAbelhas, capacidadeMel }
    });
    return response.data;
  } catch (error) {
    console.error("Erro ao criar colmeia:", error.message);
    if (error.response) {
      console.error("Detalhes:", error.response.data);
    }
  }
}

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

async function adicionarAbelha(idColmeia, quantidade, rainhaPresente) {
  try {
    const response = await axios.post(`${BASE_URL}/colmeia_add_abelhas`, null, {
      params: { idColmeia, quantidade, rainhaPresente }
    });
    return response.data;
  } catch (error) {
    console.error("Erro ao adicionar abelha:", error.message);
    if (error.response) {
      console.error("Detalhes:", error.response.data);
    }
  }
}

async function removerColmeia(idColmeia) {
  try {
    const response = await axios.delete(`${BASE_URL}/colmeia_del/${idColmeia}`);
    return response.data || "Colmeia removida com sucesso";
  } catch (error) {
    console.error("Erro ao remover colmeia:", error.message);
    if (error.response) {
      console.error("Detalhes:", error.response.data);
    }
  }
}

// Inicia o programa
main().catch(err => console.error("Erro no sistema:", err));