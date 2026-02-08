# Concienc IA

Aplicativo Android que funciona offline e online, conversando como uma "consciência" para organizar pensamentos e dúvidas. O app sempre carrega os pensamentos salvos antes de responder para manter o contexto.

## Como funciona
- **Offline:** pensado para usar modelos GGUF locais (ex.: `llama.cpp`) em uma etapa futura.
- **Online:** pronto para integrar com GPT via API.
- **Memória econômica:** pensamentos são armazenados como JSONL compactado em GZIP (`thoughts.jsonl.gz`) no armazenamento interno do app, reduzindo espaço em disco.

## Próximos passos sugeridos
- Integrar uma biblioteca GGUF (ex.: `llama.cpp` com JNI) para execução local.
- Conectar um provedor online via API com chave configurada em `local.properties` ou backend.

## Gerar APK
```bash
./gradlew assembleDebug
```
O APK será gerado em `app/build/outputs/apk/debug/app-debug.apk`.
