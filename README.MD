# Application Client - JBoss EAP 7.3

Código de exemplo para utilização do Application Client no servidor de aplicação JBoss EAP 7.3

## IDE

A IDE utilizada foi o VSCode com as seguintes extensões:
- ms-azuretools.vscode-docker
- ms-vscode-remote.remote-containers

## Imagem utilizada

A imagem utilizada para a criação do ambiente foi criada a partir do `Dockerfile` disponibilizado no repositório.
Ela inclui o settings.xml customizado e a sua instalação do JBoss Eap, ambos devem estar no workspace no mesmo nível do Dockerfile como pode ser visto nos comandos `COPY` dentro do arquivo. O nome da imagem deve ser o mesmo utilizado nos arquivos `docker-compose` que é `jboss-7.3:latest` 

## Preparação para o ambiente

- Clonar o repositório
- Abrir no VSCode
- `F1 > Remote-Containers: Open Folder in Container`
  - Escolher a pasta ServerLocal
  - Após a abertura do workspace executar a task `Create User`
    - `F1 > Taks: Run Task > Create User`
    - Escolher a opção `b` Application User
    - O nome do usuário é `non-root-user`e a senha é `P@ssw0rd`
    - Deixar as opções padrão fornecidas pelo utilitário e `yes` onde for pedido
  - Iniciar o servidor de aplicação `$EAP_HOME/bin/standalone.sh`
- Abrir uma outra janela do VSCode na raiz do workspace
- `F1 > Remote-Containers: Open Folder in Container`
  - Escolher a pasta ServerRemote
  - No shel executar `$EAP_HOME/bin/add-ser.sh -a -u 'non-root-user' -p 'P@ssw0rd' `
  - Iniciar o servidor de aplicação `$EAP_HOME/bin/standalone.sh`
- Executar o deploy da aplicação no servidor local e no remoto a partir do workspace `ServerLocal`
  - $ mvn clean install wildfly:deploy
  - $ mvn clean install wildfly:deploy -Dwildfly.hostname=serverremote -Dwildfly.username=non-root-user -Dwildfly.password=P@ssw0rd
- Executar a aplicação (Ela já começa em modo de debug, basta colocar os breakpoints)
  - `Ctrl + Shift + D > Debug - client`