# cliente_python.py
import requests

BASE_URL = "http://192.168.0.102:8080/api"

# Criar colmeia
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

# Listar colmeias
def listar_colmeias(nome_apicultor):
    response = requests.get(
        f"{BASE_URL}/colmeia_list",
        params={"nomeApicultor": nome_apicultor}
    )
    return response.json()

# adicionar abelha
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

# Remover colmeia
def remover_colmeia(id_colmeia):
    response = requests.delete(
        f"{BASE_URL}/colmeia_remove",
        params={"idColmeia": id_colmeia}
    )
    return response.json()

# Teste
if __name__ == "__main__":
    # Exemplo de uso
    print("Criando colmeia...")
    nova_colmeia = criar_colmeia("Maria", 2000, 100)
    print("Nova colmeia:", nova_colmeia)

    print("\nListando colmeias...")
    colmeias = listar_colmeias("Maria")
    print("Colmeias de Maria:", colmeias)