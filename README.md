# TODO-REST

### Neste projeto se utiliza as seguinte tecnologias

- Java 11
- Ecossistema Spring Framework
- Docker(Opcional)
- Flyway para migrations
- Conceito de DTO's
- Swagger
- E mais...

## Para subir o projeto


**Banco de dados**
- Primeira coisa que precisamos é de um banco de dados SQL Server
- Você tem a opção de utilizar banco local ou docker
	- Para criar o container pelo docker vá ate a pasta raiz do projeto pelo cmd e digite **docker compose up -d** 
	- Após isso o container deve aparecer no seu docker
- Caso opite por criar um banco por fora do docker, será necessario alterar as propriedades do banco de dados no application.yml
- Depois crie um banco de dados chamado db_todo
<br/><br/>

**Lombok** 
- Vamos instalar o lombok caso você não tenha, ele serve para evitar codigos boiler plate
- Acesse o link https://projectlombok.org/download, e faça o download 
- Abra ele com o java
- Caso ele não encontre sua IDE automaticamente, selecione o **Specify Location** e abra na onde está o executável da sua IDE
- Clique em install, reabra sua IDE, vá no menu project e depois clique em clean
	
**Subindo** 
- Logo em seguida você poderá utilizar a IDE da sua preferência para subir a aplicação
   - Exemplo STS: Clicando com o botão direito em cima do nome do projeto e Run As, **Spring Boot App**
<br/><br/>
- Você não precisa se procupar com cargas no banco pois já está utilizando migration para as cargas iniciais
- Porém todos os scripts estão disponibilizados dentro da pasta src/main/java/resources/db/migration
- Projeto de pé, na porta 8080
<br /><br/>
- Dentro da pasta de resource terá uma collection do postman com todas as chamadas REST
	
- Para acessar o swagger: http://localhost:PORTA/swagger-ui/index.html
