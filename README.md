# 🧩 Anilili Morphe Patches

Official Morphe Patch bundle for **Anilili** (`com.miruronative`).

## ❓ About

This patch bundle fixes crashes on Android 14 and Android 15+ caused by recycled bitmaps during media playback and notification creation.

### 📲 How to use these patches in Morphe Manager

Click here to add these patches as a source in Morphe Manager:  
👉 **[Add to Morphe](https://morphe.software/add-source?github=thegibbonn/morphe-patches-anilili)**

Or manually add it in Morphe Manager:
- **Settings** > **Sources** > **Add source**
- **Repository:** `thegibbonn/morphe-patches-anilili`

---

## 🩹 Patches List

<!-- PATCHES_START EXPANDED -->
- **Fix Recycled Bitmap Crash** (`com.miruronative`): Fixes recycled bitmap crashes on modern Android versions (Android 14 / 15+) during media metadata updates and notification creation. Supports all current and future versions.
<!-- PATCHES_END -->

---

## 🛠️ Building Locally

1. Create a GitHub Personal Access Token with `read:packages` scope at [GitHub Settings > Tokens](https://github.com/settings/tokens/new?scopes=read:packages&description=Morphe).
2. Add your GitHub credentials to `~/.gradle/gradle.properties`:
   ```properties
   gpr.user = thegibbonn
   gpr.key = <YOUR_GITHUB_TOKEN>
   ```
3. Build the patch bundle:
   ```bash
   ./gradlew :patches:buildAndroid
   ```
4. The generated `.mpp` bundle is output to `patches/build/libs/patches-*.mpp`.

---

## 🚀 Releasing via GitHub Actions

This repository is configured with automated semantic release workflows:
- Pushing `feat:` or `fix:` commits to `dev` generates pre-releases.
- Merging `dev` into `main` automatically publishes a new stable GitHub release with `.mpp` and `patches-bundle.json`.

---

## 📜 License

Licensed under the [GNU General Public License v3.0](LICENSE).
