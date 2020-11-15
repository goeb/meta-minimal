PR_append = "_minimal"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += "file://defconfig \
"

BUSYBOX_SPLIT_SUID = "0"

do_configure () {
    cp ${WORKDIR}/defconfig ${S}/.config
    cml1_do_configure
}


