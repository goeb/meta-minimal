SUMMARY = "A minimal rootfs image with just 1 user, 1 init script, and a login."

inherit image

IMAGE_LINGUAS = " "
LICENSE = "MIT"
IMAGE_ROOTFS_SIZE ?= "8192"
IMAGE_FSTYPES = "ext4"
NO_RECOMMENDATIONS = "1"
IMAGE_INSTALL = ""

PACKAGE_INSTALL = "base-files base-passwd busybox initscripts"

SORT_PASSWD_POSTPROCESS_COMMAND = ""
IMAGE_FEATURES = "empty-root-password allow-root-login allow-empty-password"

# Remove embedded package update-alternatives-opkg by faking a read-only rootfs
IMAGE_FEATURES += "read-only-rootfs"
