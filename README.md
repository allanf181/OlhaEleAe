# OlhaEleAe
## [DOWNLOAD](https://github.com/allanf181/OlhaEleAe/releases/download/1.0/OlhaEleAe-1.0-all.jar)

Necessário Java 17 para rodar.

Para iniciar o bot:
```cmd
java -jar OlhaEleAe-1.0-all.jar <Bot token> <Audio.ogg> <Id do Dono do Bot>
```
Nesse repositório já tem o audio em ogg, só baixar na mesma pasta do bot. 

Pagar gerar o token: https://discordpy.readthedocs.io/en/latest/discord.html  
Para pegar seu id: https://support.discord.com/hc/pt-br/articles/206346498-Onde-posso-encontrar-minhas-IDs-de-Usuário-Servidor-Mensagem-

### Comandos do bot
**Somente o dono do bot pode executar esses comandos**

``!start <nome do canal>``  
Inicia o bot no canal indicado, uma vez iniciado ele vai sempre reproduzir o audio indicado quando alguém entrar no canal.

``!stop``  
Desliga o bot.

### Problemas conhecidos

**Entrei no canal e não consegui ouvir o audio ou só ouvi uma parte.**  
Isso é um problema do Discord, existe um atraso entre você entrar no canal e você conectar no canal, o bot só consegue ver você entrar mas não consegue saber se você está conectado.
