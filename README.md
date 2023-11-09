## about

* Requires root
* probably the only sane working solution (although shitty hack)
* see more useless theory [here](https://github.com/ballerburg9005/android-nougat-vold-with-exfat-ntfs-ext4-sdcard-usb-support)
* later Android 12 and upwards versions have extFAT support (might require Magisk module) but nothing else like NTFS or ext4

## install

copy all the mystery blobs from ./binaries/ into /system/

```
adb push ./binaries/ /sdcard/
adb shell
su
mount -o rw,remount /system 
cp /sdcard/binaries/bin/* /system/bin/
cp /sdcard/binaries/lib64/* /system/lib64/
chmod 755 /system/bin/fsck.exfat /system/bin/fsck.ntfs /system/bin/mkfs.exfat /system/bin/mkfs.ntfs /system/bin/mount.exfat /system/bin/mount.ntfs /system/bin/mountsd
 mount -o ro,remount /system
 ```
 
Now install the apk ( AndroidStudioSource/app/release/app-release.apk ). All it does is to invoke the script "mountsd", which is a brute force mount script (because Android utils are that unreliable).
 
Suppose you wanted to mount an exfat formatted SD-card. It will now be mounted in /sdcard/_SDCARD . You can read this directory from apps immediately as if it was on your internal SD card without having to grant additional storage access.

## trouble

If there is no /sdcard/_sda1 or similar directory to be found, then the script failed somehow and you have to rewrite it or make your own script from scratch. Try to execute the long mount command that contains the line "context=u:object_r:fuse:s0" directly. This option is required for SELinux btw. Be sure to pick the right "mount.XXXX" for your filesystem. Like so:

```
mkdir /sdcard/_SDCARD
mount.ntfs -o rw,dirsync,nosuid,nodev,noexec,relatime,uid=1023,gid=1023,umask=0000,context=u:object_r:fuse:s0 /dev/block/mmcblk1p1 /sdcard/_SDCARD
```
*Mounting the filesystem like this is the first important thing that the mountsd script does.*

The second point of failure are the 3 special bind mount directories, which might be in a different location for your Android version. 

```
mount -o bind /sdcard/_SDCARD /mnt/runtime/write/emulated/0/_SDCARD
mount -o bind /sdcard/_SDCARD /mnt/runtime/read/emulated/0/_SDCARD
mount -o bind /sdcard/_SDCARD /mnt/runtime/default/emulated/0/_SDCARD
```

If you can read the directory from adb shell (no root) or from the File Manager app, but other apps show an empty directory, then you need to fix this bind mounting. Try a FAT32 formatted sdcard, and then check the "mount" output and steal the correct directories from there.

Lastly, you should know that the handling of SD cards has changed somewhere between Android 4 and 6 or so. So if your phone is really that old, you might have to do a few different things.
