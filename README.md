# Api de socio torcedor
> Os projetos contemplam um desafio de java.

- Maven
- Java 8+

### Instalação

- git clone 

## Api-Socio_torcedor
- **Swagger:** http://localhost:8080/swagger-ui.html
- **h2-database:** http://localhost:8080/h2-console


#### Objeto de criação esperado pelo backend:

```
{ 
	"nomeCompleto": string, 
	"email": string, 
	"dataNascimento": string["dd/MM/yyyy"], 
	"time": Integer
}
```

#### Rotas

```
GET => "/desafio/v1/sociostorcedores" (Obter todos)
POST => "/desafio/v1/sociostorcedores" (Cadastrar)
GET => "/desafio/v1/sociostorcedores/{id}" (Obter por id)
GET => "/desafio/v1/sociostorcedores/page" (Obter todos paginado)
DELETE => "/desafio/v1/sociostorcedores/{id}" (Deletar por id)
PUT => "/desafio/v1/sociostorcedores/{id}" (Alterar por id)
```
> Todas as rotas são facilmente acessadas no swagger.
