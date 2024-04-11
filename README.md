API de Gerenciamento de Tarefas
===============================

Este repositório contém um projeto de API simples para gerenciamento de tarefas, desenvolvido utilizando Java Spring. O objetivo deste projeto é praticar e compartilhar como você pode criar todos os métodos CRUD usando Java Spring.

Este projeto foi desenvolvido durante uma transmissão ao vivo no meu canal do YouTube.

Índice
------

*   [Instalação](#instala%C3%A7%C3%A3o)
*   [Configuração](#configura%C3%A7%C3%A3o)
*   [Endpoints da API](#endpoints-da-api)
*   [Banco de Dados](#banco-de-dados)

Instalação
----------

Clone o repositório:

bashCopy code

`$ git clone https://github.com/seu-usuario/nome-do-repositorio.git`

Instale as dependências com o Maven:

bashCopy code

`$ mvn install`

Configuração
------------

Modifique o arquivo application.properties para configurar a conexão com o banco de dados e outras propriedades da aplicação.

Endpoints da API
----------------

A API fornece os seguintes endpoints:

*   **GET api/tasks**: Recupera uma lista de todas as tarefas.
*   **GET api/tasks/{id}**: Recupera uma tarefa de acordo com o id.
*   **POST api/tasks**: Registra uma nova tarefa.
*   **PUT api/tasks/{id}**: Atualiza a descrição de uma tarefa existente.
*   **PUT api/tasks/{id}/{priority}**: Atualiza a prioridade de uma tarefa existente.
*   **DELETE api/tasks/{id}**: Exclui uma tarefa.

Banco de Dados
--------------

O projeto utiliza o PostgreSQL como banco de dados. As migrações necessárias do banco de dados são gerenciadas pelo Flyway.

Para instalar o PostgreSQL localmente, você pode clicar aqui.