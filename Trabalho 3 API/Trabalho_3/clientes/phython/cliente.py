import requests

BASE_IP = "localhost"  # IP do servidor
BASE_URL = f"http://{BASE_IP}:8080/api"

def criar_colmeia(nome_apicultor, capacidade_abelhas, capacidade_mel):
    response = requests.post(
        f"{BASE_URL}/colmeia_new",
        params={
            "nomeApicultor": nome_apicultor,
            "capacidadeAbelhas": capacidade_abelhas,
            "capacidadeMel": capacidade_mel
        }
    )
    return response.json()

def listar_colmeias(nome_apicultor):
    response = requests.get(
        f"{BASE_URL}/colmeia_list",
        params={"nomeApicultor": nome_apicultor}
    )
    return response.json()

def adicionar_abelha(id_colmeia, quantidade, rainha_presente):
    response = requests.post(
        f"{BASE_URL}/colmeia_add_abelhas",
        params={
            "idColmeia": id_colmeia,
            "quantidade": quantidade,
            "rainhaPresente": rainha_presente
        }
    )
    return response.json()

def remover_colmeia(id_colmeia):
    response = requests.delete(
        f"{BASE_URL}/colmeia_del/{id_colmeia}"  # Corrigido o endpoint
    )
    if response.status_code == 200:
        return {"status": "success", "message": "Colmeia removida com sucesso"}
    else:
        return {"status": "error", "message": response.text}

def menu():
    print("\n{ MENU }")
    print("{1} Criar colmeia")
    print("{2} Listar colmeias")
    print("{3} Adicionar abelhas")
    print("{4} Remover colmeia")
    print("{0} Sair")

if __name__ == "__main__":
    while True:
        menu()
        escolha = input("{Escolha uma opção}: ")
        if escolha == "1":
            nome = input("{Nome do apicultor}: ")
            cap_abelhas = int(input("{Capacidade de abelhas}: "))
            cap_mel = int(input("{Capacidade de mel}: "))
            resultado = criar_colmeia(nome, cap_abelhas, cap_mel)
            print("{Resultado}: \n", resultado)
        elif escolha == "2":
            nome = input("{Nome do apicultor}: ")
            resultado = listar_colmeias(nome)
            print("{Colmeias}: ", resultado)
        elif escolha == "3":
            id_colmeia = int(input("{ID da colmeia}: "))
            quantidade = int(input("{Quantidade de abelhas}: "))
            rainha = input("{Rainha presente? (true/false)}: ").lower() == "true"
            resultado = adicionar_abelha(id_colmeia, quantidade, rainha)
            print("{Resultado}: ", resultado)
        elif escolha == "4":
            id_colmeia = int(input("{ID da colmeia}: "))
            resultado = remover_colmeia(id_colmeia)
            print("{Resultado}: ", resultado)
        elif escolha == "0":
            print("{Saindo...}")
            break
        else:
            print("{Opção inválida, tente novamente.}")