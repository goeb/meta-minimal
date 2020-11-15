SUMMARY = "Init scripts"
DESCRIPTION = "This package includes init scripts"
LICENSE = "CLOSED"

SRC_URI = "file://rcS.d/ \
           file://inittab \
           file://fstab \
          "

S = "${WORKDIR}"

do_install() {

	# /etc/rcS.d
	install -d ${D}${sysconfdir}/rcS.d
	cp -a ${WORKDIR}/rcS.d/* ${D}${sysconfdir}/rcS.d/.
	chown -R root:root ${D}${sysconfdir}/rcS.d
	chmod -R 0544 ${D}${sysconfdir}/rcS.d

	install -m 0644 ${WORKDIR}/inittab ${D}${sysconfdir}/.
	install -m 0644 ${WORKDIR}/fstab ${D}${sysconfdir}/.
}
