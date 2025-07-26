#include <stdio.h>
#include <stdbool.h>
#include <curl/curl.h>
#include <string.h>

#define API_URL "http://172.25.210.208:8080/api"

size_t write_callback(void *contents, size_t size, size_t nmemb, void *userp) {
    size_t total_size = size * nmemb;
    printf("%.*s", (int)total_size, (char *)contents);
    return total_size;
}

void criar_colmeia(const char *nome_apicultor, int capacidade_abelhas, int capacidade_mel) {
    CURL *curl = curl_easy_init();
    if (curl) {
        char url[256];
        snprintf(url, sizeof(url), "%s/colmeia_new?nomeApicultor=%s&capacidadeAbelhas=%d&capacidadeMel=%d",
                 API_URL, nome_apicultor, capacidade_abelhas, capacidade_mel);

        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_POST, 1L);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
        curl_easy_setopt(curl, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, "");
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 5L);

        struct curl_slist *headers = NULL;
        headers = curl_slist_append(headers, "Expect:");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        printf("{Enviando POST para criar colmeia...}\n");
        CURLcode res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            fprintf(stderr, "{Erro ao criar colmeia: %s}\n", curl_easy_strerror(res));
        }
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    }
}

void listar_colmeias(const char *nome_apicultor) {
    CURL *curl = curl_easy_init();
    if (curl) {
        char url[256];
        snprintf(url, sizeof(url), "%s/colmeia_list?nomeApicultor=%s", API_URL, nome_apicultor);

        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);

        printf("{Enviando GET para listar colmeias...}\n");
        CURLcode res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            fprintf(stderr, "{Erro ao listar colmeias: %s}\n", curl_easy_strerror(res));
        }
        curl_easy_cleanup(curl);
    }
}

void adicionar_abelha(int id_colmeia, int quantidade, bool rainha_presente) {
    CURL *curl = curl_easy_init();
    if (curl) {
        char url[256];
        const char *rainha_str = rainha_presente ? "true" : "false";
        snprintf(url, sizeof(url), "%s/colmeia_add_abelhas?idColmeia=%d&quantidade=%d&rainhaPresente=%s",
                 API_URL, id_colmeia, quantidade, rainha_str);

        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_POST, 1L);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
        curl_easy_setopt(curl, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, "");
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 5L);

        struct curl_slist *headers = NULL;
        headers = curl_slist_append(headers, "Expect:");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        printf("{Enviando POST para adicionar abelhas...}\n");
        CURLcode res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            fprintf(stderr, "{Erro ao adicionar abelhas: %s}\n", curl_easy_strerror(res));
        }
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    }
}

void remover_colmeia(int id_colmeia) {
    CURL *curl = curl_easy_init();
    if (curl) {
        char url[256];
        // Corrigido o endpoint para usar path parameter (alinhado com o Spring Boot)
        snprintf(url, sizeof(url), "%s/colmeia_del/%d", API_URL, id_colmeia);

        // Configurações do CURL
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_CUSTOMREQUEST, "DELETE");
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 5L);
        
        // Headers (opcional, mas recomendado)
        struct curl_slist *headers = NULL;
        headers = curl_slist_append(headers, "Content-Type: application/json");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        printf("Enviando DELETE para remover colmeia ID %d...\n", id_colmeia);
        CURLcode res = curl_easy_perform(curl);
        
        // Tratamento de erros aprimorado
        if (res != CURLE_OK) {
            fprintf(stderr, "Erro na requisição: %s\n", curl_easy_strerror(res));
        } else {
            long http_code = 0;
            curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, &http_code);
            
            if (http_code == 200) {
                printf("Colmeia %d removida com sucesso!\n", id_colmeia);
            } else {
                fprintf(stderr, "Falha ao remover. HTTP Status: %ld\n", http_code);
            }
        }
        
        // Limpeza
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    } else {
        fprintf(stderr, "Falha ao inicializar libcurl.\n");
    }
}

void menu() {
    printf("\n{ MENU }\n");
    printf("{1} Criar colmeia\n");
    printf("{2} Listar colmeias\n");
    printf("{3} Adicionar abelhas\n");
    printf("{4} Remover colmeia\n");
    printf("{0} Sair\n");
}

int main() {
    curl_global_init(CURL_GLOBAL_ALL);

    int escolha;
    while (1) {
        menu();
        printf("{Escolha uma opção}: ");
        if (scanf("%d", &escolha) != 1) break;
        getchar(); // Limpa o buffer do teclado

        if (escolha == 1) {
            char nome[100];
            int cap_abelhas, cap_mel;
            printf("{Nome do apicultor}: ");
            fgets(nome, sizeof(nome), stdin);
            nome[strcspn(nome, "\n")] = 0;
            printf("{Capacidade de abelhas}: ");
            scanf("%d", &cap_abelhas);
            printf("{Capacidade de mel}: ");
            scanf("%d", &cap_mel);
            getchar();
            criar_colmeia(nome, cap_abelhas, cap_mel);
        } else if (escolha == 2) {
            char nome[100];
            printf("{Nome do apicultor}: ");
            fgets(nome, sizeof(nome), stdin);
            nome[strcspn(nome, "\n")] = 0;
            listar_colmeias(nome);
        } else if (escolha == 3) {
            int id_colmeia, quantidade;
            char rainha[10];
            bool rainha_presente;
            printf("{ID da colmeia}: ");
            scanf("%d", &id_colmeia);
            printf("{Quantidade de abelhas}: ");
            scanf("%d", &quantidade);
            getchar();
            printf("{Rainha presente? (true/false)}: ");
            fgets(rainha, sizeof(rainha), stdin);
            rainha[strcspn(rainha, "\n")] = 0;
            rainha_presente = (strcmp(rainha, "true") == 0);
            adicionar_abelha(id_colmeia, quantidade, rainha_presente);
        } else if (escolha == 4) {
            int id_colmeia;
            printf("{ID da colmeia}: ");
            scanf("%d", &id_colmeia);
            getchar();
            remover_colmeia(id_colmeia);
        } else if (escolha == 0) {
            printf("{Saindo...}\n");
            break;
        } else {
            printf("{Opção inválida!}\n");
        }
    }

    curl_global_cleanup();
    return 0;
}