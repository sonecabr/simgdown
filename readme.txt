Downloader simples de arquivos publicos de um servidor http

Parametros
0 - Json de referencia - > um arquivo contendo a string no seguinte padrao [{"id":"IDn", "url":"URLn"},{"id":"IDn+1", "url":"URLn+1"}]
1 - Pasta de saida -> fornecer uma pasta do sistema onde o usuario q executou o programa tenha acesso de escrita
2[opcional] - nova url -> nova url para geracao do novo json contendo os arquivos em questao

Resultado

- Os arquivos contidos no json serao baixados para  a pasta de saida informada  e um novo json contendo as novas urls e gerado
