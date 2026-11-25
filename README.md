# Blue Light Filter Pro (Android)

A starter native Android app with a premium dark interface, manual ON/OFF, adjustable filter strength, and selectable custom tint presets. It uses an Android overlay so the tint can appear above other apps.

## Build APK
1. Install Android Studio (latest stable) and Android SDK Platform 35.
2. Open this `BlueLightFilter` folder in Android Studio.
3. Allow Gradle sync to finish.
4. Choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. Find the debug APK at `app/build/outputs/apk/debug/app-debug.apk`.
6. Transfer it to the Android phone and install. If prompted, allow installation from that source.
7. On first use, grant **Display over other apps** permission, return to the app, then tap **TURN FILTER ON**.

## Notes
- This is a starter implementation; test on the target phone before daily use.
- The app requests overlay permission because Android does not allow ordinary apps to apply a system-wide screen tint without it.
- Filter tint and intensity are software overlays; they do not change the phone's hardware blue-light emission or guarantee eye-health benefits.
- Some manufacturers restrict background services or overlay behavior. Battery optimization settings may affect persistence.
- The app is manual-only; no automatic schedule is included, as requested.
