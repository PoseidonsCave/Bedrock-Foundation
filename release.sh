#!/usr/bin/env bash
set -e

JAR_PATH="build/libs/Bedrock-Foundation-1.0.0-demo.jar"
DIST_DIR="dist"
LICENSE_DIR="licenses"
RANKS=("INIT" "DEV")

# Helper to lowercase strings
tolower() {
  echo "$1" | tr '[:upper:]' '[:lower:]'
}

echo "🔍 Checking for source changes..."
./gradlew clean build -q
echo "✅ Build complete: $JAR_PATH"

echo "🔑 Generating mock licenses..."
mkdir -p "$LICENSE_DIR"

for rank in "${RANKS[@]}"; do
  echo " → Generating $rank license ..."
  java -cp "$JAR_PATH" com.bedrock.core.Engine "$rank" >/dev/null 2>&1 || true
  lc_rank=$(tolower "$rank")
  LICENSE_FILE="${LICENSE_DIR}/${lc_rank}_license.json"
  if [[ -f "$LICENSE_FILE" ]]; then
    echo "   ✅ License created: $LICENSE_FILE"
  else
    echo "   ⚠️ License for $rank not found!"
  fi
done

echo "📦 Packaging releases..."
mkdir -p "$DIST_DIR"
for rank in "${RANKS[@]}"; do
  lc_rank=$(tolower "$rank")
  LICENSE_FILE="${LICENSE_DIR}/${lc_rank}_license.json"
  RELEASE_ZIP="${DIST_DIR}/${lc_rank}_release.zip"
  if [[ -f "$LICENSE_FILE" ]]; then
    zip -j "$RELEASE_ZIP" "$JAR_PATH" "$LICENSE_FILE" >/dev/null
    echo "Created: $RELEASE_ZIP"
  fi
done

echo ""
echo "✅ Demo release complete! Packages available in → $DIST_DIR/"