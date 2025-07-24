#include <stdio.h>
#include <stdbool.h> 
#include <curl/curl.h>

// URL base da API (substitua pelo IP do servidor Spring Boot)
#define API_URL "http://192.168.0.102:8080/api"

// Função para lidar com a resposta do servidor
size_t write_callback(void *contents, size_t size, size_t nmemb, void *userp) {
    size_t total_size = size * nmemb;
    printf("%.*s", (int)total_size, (char *)contents);
    return total_size;
}

// Criar colmeia (POST)
void criar_colmeia(const char *nome_apicultor, int capacidade_abelhas, int capacidade_mel) {
    
    CURL *curl = curl_easy_init();

    if (curl) {
        char url[256];
        snprintf(url, sizeof(url), "%s/colmeia_new?nomeApicultor=%s&capacidadeAbelhas=%d&capacidadeMel=%d", 
                 API_URL, nome_apicultor, capacidade_abelhas, capacidade_mel);

        // Configurações essenciais
        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_POST, 1L);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
        curl_easy_setopt(curl, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, "");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, "Content-Length: 0");
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 5L);

        struct curl_slist *headers = NULL;
        headers = curl_slist_append(headers, "Expect:");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        printf("Enviando POST...\n");
        CURLcode res = curl_easy_perform(curl);
        
        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    }
}

// Listar colmeias (GET)
void listar_colmeias(const char *nome_apicultor) {
    CURL *curl = curl_easy_init();
    if (curl) {
        char url[256];
        snprintf(url, sizeof(url), "%s/colmeia_list?nomeApicultor=%s", API_URL, nome_apicultor);

        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);

        printf("Enviando GET...\n");
        CURLcode res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            fprintf(stderr, "Erro no GET: %s\n", curl_easy_strerror(res));
        }

        curl_easy_cleanup(curl);
    }
}

void adicionar_abelha(int id_colmeia, int quantidade, bool rainha_presente) {
    CURL *curl = curl_easy_init();
    if (curl) {
        char url[256];
        const char *rainha_str = rainha_presente ? "true" : "false";
        
        snprintf(url, sizeof(url), "%s/colmeia/%d/colmeia_add_abelhas?quantidade=%d&rainhaPresente=%s", 
                 API_URL, id_colmeia, quantidade, rainha_str);

        curl_easy_setopt(curl, CURLOPT_URL, url);
        curl_easy_setopt(curl, CURLOPT_POST, 1L);
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_callback);
        curl_easy_setopt(curl, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, "");
        curl_easy_setopt(curl, CURLOPT_TIMEOUT, 5L);

        struct curl_slist *headers = NULL;
        headers = curl_slist_append(headers, "Content-Length: 0");
        headers = curl_slist_append(headers, "Expect:");
        curl_easy_setopt(curl, CURLOPT_HTTPHEADER, headers);

        printf("Enviando POST para adicionar %d abelhas à colmeia %d (rainha: %s)...\n", 
               quantidade, id_colmeia, rainha_str);
               
        CURLcode res = curl_easy_perform(curl);
        if (res != CURLE_OK) {
            fprintf(stderr, "Erro no POST: %s\n", curl_easy_strerror(res));
        } else {
            long http_code = 0;
            curl_easy_getinfo(curl, CURLINFO_RESPONSE_CODE, &http_code);
            printf("HTTP Status: %ld\n", http_code);
        }

        curl_slist_free_all(headers);
        curl_easy_cleanup(curl);
    }
}

int main() {
    curl_global_init(CURL_GLOBAL_ALL);

    // Exemplo de uso
    printf("Iniciando cliente...\n");
    criar_colmeia("Joao", 2000, 100);
    printf("Colmeia criada com sucesso!\n");
    
    printf("Listando colmeias...\n");
    listar_colmeias("Joao");
    printf("\nListagem concluída!\n");
    
    printf("Adicionando abelhas...\n");
    adicionar_abelha(1, 50, true); // Adiciona 50 abelhas com rainha presente
    printf("Abelhas adicionadas com sucesso!\n");
    
    curl_global_cleanup();
    
    return 0;
}