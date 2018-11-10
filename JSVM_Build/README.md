# JSVM

Clone the repo: `git clone https://github.com/floriandejonckheere/jsvm.git`

## Build for Android

1. Make appropriate toolchain using $(NDK_ROOT)/make_standalone_toolchain.py --install-dir=$(INSTALL_DIR) --api $(API_VERSION) --arch $(ARCH)
2. Replace system compilers gcc, g+, asm with corresponding architecture's compilers found in $(INSTALL_DIR)/bin/
3. Remove/Comment `-lpthread` flag in all make files (jsvm/JSVM/H264Extensions/build/linux/). Add additional CPP flags to prevent warnings: `-Wno-sign-compare -Wno-c++11-narrowing -Wno-literal-range -Wno-reserved-user-defined-literal`
4. Add `-static-libstdc++` linker flag in static debug and release [Key: $(LD) -static-libstdc++] ( So that executable uses the static libstd file instead of a shared file)
5. Build it: `make` (make -j2/ make -j4 is not advisable )

## Test case:

Phone: `Moto G4 Play running API 26 (7.1.1) armv7-a architecture: Non-rooted`

1. `~: $(NDK_ROOT)/make_standalone_toolchain.py --install-dir=~/toolchain32 --api 26 --arch arm `
2. `toolchain = ~/toolchain32/bin
    name = arm-linux-androideabi
    CPP	= $(toolchain)/$(name)-g++ `
3. Remove/Comment `-lpthread` flag in all make files (jsvm/JSVM/H264Extensions/build/linux/). Add additional CPP flags to prevent warnings: `-Wno-sign-compare -Wno-c++11-narrowing -Wno-literal-range -Wno-reserved-user-defined-literal`
4. Add `-static-libstdc++` linker flag in static debug and release [Key: $(LD) -static-libstdc++]
5. Build it: `make` (make -j2/ make -j4 is not advisable )

The executables are found in `jsvm/bin/`

Test the executables through adb:

1. `adb push DownConvertStatic /data/local/tmp`
(Note: This is the only directory in a non-rooted device that has execute permission)
2. `abd shell`
3. `cd /data/local/tmp`
4. `./DownConvertStatic --help`
