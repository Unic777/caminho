# Concienc IA

Aplicativo Android que funciona offline e online, conversando como uma "consciência" para organizar pensamentos e dúvidas. O app sempre carrega os pensamentos salvos antes de responder para manter o contexto.

## Como funciona
- **Offline:** escolha um arquivo GGUF (ex.: armazenado em Downloads) para futura execução local.
- **Online:** informe a chave de API para integrar com GPT.
- **Memória econômica:** pensamentos são armazenados como JSONL compactado em GZIP (`thoughts.jsonl.gz`) no armazenamento interno do app, reduzindo espaço em disco.

## Uso rápido
1. Abra o app e use **Selecionar GGUF (Downloads)** para apontar para o arquivo do modelo.
2. Cole a **chave de API** e toque em **Salvar chave** quando for usar IA online.
3. Escreva suas dúvidas e pensamentos e toque em **Enviar**.

## Próximos passos sugeridos
- Integrar uma biblioteca GGUF (ex.: `llama.cpp` com JNI) para execução local.
- Conectar um provedor online via API com chave configurada em `local.properties` ou backend.

## Gerar APK
### Opção 1: Android Studio (mais simples)
1. Instale o Android Studio.
2. Abra este projeto.
3. Aguarde o download do **Android SDK** e ferramentas de build.
4. No menu, escolha **Build > Build Bundle(s) / APK(s) > Build APK(s)**.

### Opção 2: Linha de comando
1. Instale o Android SDK (incluindo **platform-tools** e **build-tools**).
2. Crie/ajuste o arquivo `local.properties` com o caminho do SDK:
   ```properties
   sdk.dir=/caminho/para/Android/Sdk
   ```
3. Execute:
   ```bash
   gradle :app:assembleDebug
   ```
   Ou use o script:
   ```bash
   ./scripts/build-apk.sh
   ```

O APK será gerado em `app/build/outputs/apk/debug/app-debug.apk`.
