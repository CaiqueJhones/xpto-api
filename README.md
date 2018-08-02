XTPO API
========

API Rest para gerenciar uma lista de cidades do Brasil.

## Pilha de tecnologias

* Spring Boot
* Banco de dados H2

## Como utilizar

Execute a aplicação através da linha de comando:

```
gradlew bootRun
```

ou gere um .jar com:

```
gradlew assemble
```

ou ainda pode executar os testes unitários com o comando: 

```
gradlew test
```

e a cobertura de teste:

```
gradlew jacocoTestReport
```

### Rotas da API

01. Obter a lista de cidades que são capitais ordenadas pelo nome:

```
GET http://localhost:5000/capitals
```

02. Obter o nome do estado com a maior e menor quantidade de cidades e a quantidade de cidades:

```
GET http://localhost:5000/largest-and-smallest
```

03. Obter a quantidade de cidades por estado:

```
GET http://localhost:5000/states
```

04. Obter os dados da cidade informando o id do IBGE:

```
GET http://localhost:5000/:ibge_id
```

05. Obter o nome das cidades baseado em um estado selecionado:

```
GET http://localhost:5000/names?uf=:name
```

06. Adicionar uma nova Cidade:

```
POST http://localhost:5000/
```

e no corpo da requisição os seguintes dados de exemplo:

```
{
    "ibge_id": 100,
    "uf": "EX",
    "name": "Example",
    "capital": false,
    "lon": -72.9165010261,
    "lat": -7.5932225939,
    "no_accents": "Example",
    "alternative_name": "",
    "microregion": "Example",
    "mesoregion": "Example"
}
```

07. Deletar uma cidade:

```
DELETE http://localhost:5000/:ibge_id
```

08. Selecionar uma coluna e através dela entrar com uma string para filtrar. Retornar assim todos os objetos que contenham tal string:

```
GET http://localhost:5000/filter?column=:column&q=:query
```

A lista de colunas disponíveis são:

* ibge_id (número inteiro)
* uf (string)
* name (string)
* capital (true ou false)
* no_accents (string)
* alternative_names (string)
* microregion (string)
* mesoregion (string)
* lat (número decimal)
* lon (número decimal)

09. Obter a quantidade de registro baseado em uma coluna. Não conta itens iguais:

```
GET http://localhost:5000/count?column=:column
```

10. A quantidade de registros total:

```
GET http://localhost:5000/count-all
```

11. Obter dentre todas as cidades, as duas cidades mais distantes uma da outra com base na localização (distância em KM em linha reta):

```
GET http://localhost:5000/further
```

O primeiro acesso é feito o cálculo das distâncias, com isso o resultado pode levar vários segundos. Posteriormente o resultado é armazenado em Cache.

12. Acessar o console do H2

```
http://localhost:5000/h2-console
```