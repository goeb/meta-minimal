# A minimal Linux image

Purpose: illustrate how to use Yocto to build a minimalist rootfs.

## Features:

- minimal Root FS (busybox only, no network)
- one init script
- one user (root, empty password)

## Embedded Packages

Manifest:
```
base-files qemux86_64 3.0.14b-r0
busybox core2-64 1.32.0-r0_minimal
initscripts core2-64 1.1-r0
libc6 core2-64 2.32-r0
```

## Requirements:

- Yocto branch gatesgarth (yocto-3.2)

## Sizes:

- bzImage: 8.8 MBytes
- Root FS: 4.4 MBytes

```
root@qemux86-64:~# df -h /
Filesystem                Size      Used Available Use% Mounted on
/dev/root                 6.7M      4.4M      1.7M  72% /
```


# How to build and execute

## Build and execute
```
TOP$ git clone --branch yocto-3.2 git://git.yoctoproject.org/poky
TOP$ git clone https://github.com/goeb/meta-minimal.git
TOP$ . meta-minimal/env
TOP/build$ bitbake image-minimal
TOP/build$ qemu-system-x86_64 -cpu core2duo -m 256 \
    -drive file=tmp-glibc/deploy/images/qemux86-64/image-minimal-qemux86-64.ext4,if=virtio,format=raw \
    -kernel tmp-glibc/deploy/images/qemux86-64/bzImage \
    -append 'root=/dev/vda rw mem=256M console=ttyS0' \
    -serial mon:stdio
```

## Execution output

```
[    0.000000] Linux version 5.8.13-yocto-standard (oe-user@oe-host) (x86_64-oe-linux-gcc (GCC) 10.2.0, GNU ld (GNU Binutils) 2.35.0.20200730) #1 SMP PREEMPT Sat Nov 14 09:26:52 UTC 2020
[    0.000000] Command line: root=/dev/vda rw mem=256M console=ttyS0

...

[    4.672086] Run /sbin/init as init process
[    4.806565] EXT4-fs (vda): re-mounted. Opts: (null)
[    4.806844] ext4 filesystem being remounted at / supports timestamps until 2038 (0x7fffffff)
starting pid 93, tty '': '/etc/init.d/rcS'
-----------------------------------------
|         Welcome to minimal            |
-----------------------------------------
starting pid 95, tty '/dev/ttyS0': '/sbin/getty 115200 /dev/ttyS0'

Yocto Minimal
OpenEmbedded nodistro.0 qemux86-64 /dev/ttyS0

qemux86-64 login: root
login[96]: root login on 'ttyS0'
root@qemux86-64:~# uname -a
Linux qemux86-64 5.8.13-yocto-standard #1 SMP PREEMPT Sat Nov 14 20:38:31 UTC 2020 x86_64 GNU/Linux
root@qemux86-64:~# 
```

## Mounts

```
root@qemux86-64:~# mount
/dev/root on / type ext4 (ro,relatime)
devtmpfs on /dev type devtmpfs (rw,relatime,size=116104k,nr_inodes=29026,mode=755)
proc on /proc type proc (rw,relatime)
devpts on /dev/pts type devpts (rw,relatime,gid=5,mode=620,ptmxmode=000)
tmpfs on /dev/shm type tmpfs (rw,relatime,mode=777)
tmpfs on /tmp type tmpfs (rw,relatime)
tmpfs on /run type tmpfs (rw,nosuid,nodev,relatime)
sysfs on /sys type sysfs (rw,relatime)
```

## Embedded Files
```
root@qemux86-64:~# ls -l /
total 24
drwxr-xr-x    2 root     root          4096 Nov 14 20:59 bin
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 boot
drwxr-xr-x    8 root     root          2620 Nov 15 14:28 dev
drwxr-xr-x    6 root     root          1024 Nov 14 20:59 etc
drwxr-xr-x    3 root     root          1024 Nov 14 20:36 home
drwxr-xr-x    2 root     root          1024 Nov 14 20:59 lib
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 linuxrc -> /bin/busybox
drwx------    2 root     root         12288 Nov 14 20:59 lost+found
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 mnt
dr-xr-xr-x  107 root     root             0 Nov 15 14:28 proc
drwxrwxrwt    2 root     root            60 Nov 15 14:28 run
drwxr-xr-x    2 root     root          1024 Nov 14 20:59 sbin
dr-xr-xr-x   12 root     root             0 Nov 15 14:28 sys
drwxrwxrwt    2 root     root            40 Nov 15 14:28 tmp
drwxr-xr-x    7 root     root          1024 Nov 14 20:36 usr
drwxr-xr-x    6 root     root          1024 Nov 14 20:59 var
```


```
root@qemux86-64:~# find /bin /boot /etc /home /lib /mnt /sbin /tmp /usr /var -exec ls -ld {} \;
drwxr-xr-x    2 root     root          4096 Nov 14 20:59 /bin
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/yes -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/setpriv -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/tty -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/false -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/bc -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/timeout -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/logger -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/id -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/true -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/cp -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/chmod -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/dnsdomainname -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/setfattr -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/[ -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/seq -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/rmdir -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/cut -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/setsid -> /bin/busybox
-rwsr-xr-x    1 root     root        599048 Nov 14 20:54 /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/echo -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/resume -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/more -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/watch -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/umount -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/groups -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/flock -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/ash -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/xxd -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/du -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/w -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/basename -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/top -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/netstat -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/expr -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/printf -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/mkpasswd -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/free -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/unlink -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/shred -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/hd -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/mv -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/nuke -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/ps -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/vi -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/fallocate -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/run-parts -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sha256sum -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/stat -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/strings -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/rm -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/mkfifo -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/[[ -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/gzip -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/stty -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/logname -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sha1sum -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/lsof -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/dirname -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sleep -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/test -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/su -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/tee -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/date -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/kill -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/fatattr -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/mount -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/ls -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sed -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/uniq -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/xargs -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/cttyhack -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/egrep -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/hexedit -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/realpath -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/reset -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/who -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/ts -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sh -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/pidof -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/truncate -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/grep -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/head -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/patch -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/unshare -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/mkdir -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/hexdump -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/microcom -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/hostname -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/nslookup -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/readlink -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/less -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/wc -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/install -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/find -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/ping6 -> /bin/busybox
lrwxrwxrwx    1 root     root             7 Nov 14 20:54 /bin/busybox.nosuid -> busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/touch -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/od -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/fgrep -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/md5sum -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/login -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/fsync -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/usleep -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/fuser -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/cat -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/which -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/whoami -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/users -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/nohup -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/dmesg -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/df -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/bzcat -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sync -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/ln -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/clear -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/tail -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/diff -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/dc -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/cmp -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/chattr -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/awk -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/killall -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/ping -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/uptime -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sha512sum -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/time -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/uname -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sha3sum -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/tr -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/tar -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/env -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/gunzip -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/mknod -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/mktemp -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/sort -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/dumpleases -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/nsenter -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/pwd -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/chown -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/dd -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/chgrp -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/zcat -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/unzip -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /bin/traceroute -> /bin/busybox
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /boot
drwxr-xr-x    6 root     root          1024 Nov 14 20:59 /etc
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /etc/skel
-rw-r--r--    1 root     root           550 Nov 14 20:59 /etc/fstab
lrwxrwxrwx    1 root     root            12 Nov 14 20:36 /etc/mtab -> /proc/mounts
-rw-r--r--    1 root     root           919 Nov 14 20:59 /etc/ld.so.cache
-rw-r--r--    1 root     root            15 Nov 14 20:59 /etc/timestamp
-rw-r--r--    1 root     root          2045 Nov 14 20:54 /etc/busybox.links
dr-xr--r--    2 root     root          1024 Nov 14 20:50 /etc/rcS.d
-r-xr--r--    1 root     root           158 Nov 14 08:29 /etc/rcS.d/S01banner
-rw-r--r--    1 root     root            42 Nov 14 20:32 /etc/issue.net
drwxr-xr-x    2 root     root          1024 Nov 14 20:54 /etc/init.d
-rwxr-xr-x    1 root     root           437 Nov 14 20:54 /etc/init.d/rcS
-rwxr-xr-x    1 root     root           402 Nov 14 20:54 /etc/init.d/rcK
-rw-------    1 root     root            21 Nov 14 20:32 /etc/shadow
-rw-r--r--    1 root     root            10 Nov 14 08:38 /etc/group
-rw-r--r--    1 root     root            15 Nov 14 20:59 /etc/version
-rw-r--r--    1 root     root            45 Nov 14 20:32 /etc/issue
-rw-r--r--    1 root     root            35 Nov 14 15:36 /etc/passwd
-rw-r--r--    1 root     root            11 Nov 14 20:32 /etc/hostname
-rw-r--r--    1 root     root            33 Nov 14 20:44 /etc/ld.so.conf
-rw-r--r--    1 root     root           878 Nov 14 20:59 /etc/inittab
-rw-r--r--    1 root     root            42 Nov 14 20:32 /etc/shells
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /etc/default
-rw-r--r--    1 root     root           857 Nov 14 20:32 /etc/profile
drwxr-xr-x    3 root     root          1024 Nov 14 20:36 /home
drwx------    2 root     root          1024 Nov 14 20:36 /home/root
drwxr-xr-x    2 root     root          1024 Nov 14 20:59 /lib
lrwxrwxrwx    1 root     root            12 Nov 14 20:44 /lib/libc.so.6 -> libc-2.32.so
-rwxr-xr-x    1 root     root         22560 Nov 14 20:44 /lib/libnss_dns-2.32.so
-rwxr-xr-x    1 root     root         88320 Nov 14 20:44 /lib/libresolv-2.32.so
lrwxrwxrwx    1 root     root            23 Nov 14 20:44 /lib/libBrokenLocale.so.1 -> libBrokenLocale-2.32.so
lrwxrwxrwx    1 root     root            20 Nov 14 20:44 /lib/libnss_files.so.2 -> libnss_files-2.32.so
-rwxr-xr-x    1 root     root         39328 Nov 14 20:44 /lib/librt-2.32.so
lrwxrwxrwx    1 root     root            14 Nov 14 20:44 /lib/libanl.so.1 -> libanl-2.32.so
-rwxr-xr-x    1 root     root         14360 Nov 14 20:44 /lib/libdl-2.32.so
lrwxrwxrwx    1 root     root            13 Nov 14 20:44 /lib/libdl.so.2 -> libdl-2.32.so
lrwxrwxrwx    1 root     root            17 Nov 14 20:44 /lib/libresolv.so.2 -> libresolv-2.32.so
-rwxr-xr-x    1 root     root        177768 Nov 14 20:44 /lib/ld-2.32.so
-rwxr-xr-x    1 root     root         14296 Nov 14 20:44 /lib/libBrokenLocale-2.32.so
lrwxrwxrwx    1 root     root            15 Nov 14 20:44 /lib/libmvec.so.1 -> libmvec-2.32.so
-rwxr-xr-x    1 root     root       1809976 Nov 14 20:44 /lib/libc-2.32.so
-rwxr-xr-x    1 root     root         92184 Nov 14 20:44 /lib/libnsl-2.32.so
lrwxrwxrwx    1 root     root            10 Nov 14 20:44 /lib/ld-linux-x86-64.so.2 -> ld-2.32.so
lrwxrwxrwx    1 root     root            18 Nov 14 20:44 /lib/libnss_dns.so.2 -> libnss_dns-2.32.so
-rwxr-xr-x    1 root     root         47136 Nov 14 20:44 /lib/libnss_files-2.32.so
lrwxrwxrwx    1 root     root            13 Nov 14 20:44 /lib/librt.so.1 -> librt-2.32.so
lrwxrwxrwx    1 root     root            21 Nov 14 20:44 /lib/libnss_compat.so.2 -> libnss_compat-2.32.so
lrwxrwxrwx    1 root     root            18 Nov 14 20:44 /lib/libpthread.so.0 -> libpthread-2.32.so
-rwxr-xr-x    1 root     root       1312800 Nov 14 20:44 /lib/libm-2.32.so
-rwxr-xr-x    1 root     root         14360 Nov 14 20:44 /lib/libutil-2.32.so
lrwxrwxrwx    1 root     root            15 Nov 14 20:44 /lib/libutil.so.1 -> libutil-2.32.so
-rwxr-xr-x    1 root     root         35288 Nov 14 20:44 /lib/libnss_compat-2.32.so
lrwxrwxrwx    1 root     root            14 Nov 14 20:44 /lib/libnsl.so.1 -> libnsl-2.32.so
-rwxr-xr-x    1 root     root        113296 Nov 14 20:44 /lib/libpthread-2.32.so
lrwxrwxrwx    1 root     root            12 Nov 14 20:44 /lib/libm.so.6 -> libm-2.32.so
-rwxr-xr-x    1 root     root        174104 Nov 14 20:44 /lib/libmvec-2.32.so
-rwxr-xr-x    1 root     root         18696 Nov 14 20:44 /lib/libanl-2.32.so
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /mnt
drwxr-xr-x    2 root     root          1024 Nov 14 20:59 /sbin
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/i2ctransfer -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/switch_root -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/ip -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/tc -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/mim -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/reboot -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/ifdown -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/pivot_root -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/uevent -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/ubirename -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/partprobe -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/start-stop-daemon -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/losetup -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/fbset -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/setconsole -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/udhcpd -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/run-init -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/ifup -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/ifconfig -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/chroot -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/logread -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/hwclock -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/fsfreeze -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/init -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/udhcpc -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/mdev -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/route -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/nologin -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/sysctl -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/poweroff -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/getty -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/svlogd -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/halt -> /bin/busybox
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /sbin/fstrim -> /bin/busybox
drwxrwxrwt    2 root     root            40 Nov 15 14:28 /tmp
drwxr-xr-x    7 root     root          1024 Nov 14 20:36 /usr
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /usr/src
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /usr/sbin
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /usr/share
drwxr-xr-x    2 root     root          1024 Nov 14 20:59 /usr/lib
drwxr-xr-x    2 root     root          1024 Nov 14 20:59 /usr/bin
lrwxrwxrwx    1 root     root            12 Nov 14 20:59 /usr/bin/link -> /bin/busybox
drwxr-xr-x    6 root     root          1024 Nov 14 20:59 /var
lrwxrwxrwx    1 root     root            12 Nov 14 20:36 /var/log -> volatile/log
lrwxrwxrwx    1 root     root            11 Nov 14 20:36 /var/lock -> ../run/lock
lrwxrwxrwx    1 root     root             6 Nov 14 20:36 /var/run -> ../run
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /var/local
drwxr-xr-x    3 root     root          1024 Nov 14 20:59 /var/lib
drwxr-xr-x    2 root     root          1024 Nov 14 20:59 /var/lib/opkg
drwxr-xr-x    4 root     root          1024 Nov 14 20:59 /var/cache
drwxr-xr-x    2 root     root          1024 Nov 14 20:59 /var/cache/opkg
drwx------    2 root     root          1024 Nov 14 20:59 /var/cache/ldconfig
-rw-------    1 root     root           971 Nov 14 20:59 /var/cache/ldconfig/aux-cache
lrwxrwxrwx    1 root     root            12 Nov 14 20:36 /var/tmp -> volatile/tmp
drwxr-xr-x    4 root     root          1024 Nov 14 20:36 /var/volatile
drwxr-xr-x    2 root     root          1024 Nov 14 20:36 /var/volatile/log
drwxrwxrwt    2 root     root          1024 Nov 14 20:36 /var/volatile/tmp
```

