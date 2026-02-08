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
```bash
./gradlew assembleDebug
```
O APK será gerado em `app/build/outputs/apk/debug/app-debug.apk`.
