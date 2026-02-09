#!/usr/bin/env bash
set -euo pipefail

if [[ -z "${ANDROID_HOME:-}" && -z "${ANDROID_SDK_ROOT:-}" ]]; then
  echo "Erro: ANDROID_HOME ou ANDROID_SDK_ROOT não definido."
  echo "Configure o caminho do SDK antes de continuar."
  exit 1
fi

if [[ ! -f "local.properties" ]]; then
  SDK_PATH="${ANDROID_HOME:-${ANDROID_SDK_ROOT}}"
  echo "sdk.dir=${SDK_PATH}" > local.properties
fi

gradle :app:assembleDebug

echo "APK gerado em: app/build/outputs/apk/debug/app-debug.apk"
