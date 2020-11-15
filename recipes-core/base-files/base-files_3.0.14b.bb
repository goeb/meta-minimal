SUMMARY = "Miscellaneous files for the base system"
DESCRIPTION = "The base-files package creates the basic system directory structure and provides a small set of key configuration files for the system."
SECTION = "base"

LICENSE = "CLOSED"

SRC_URI = "file://profile \
           file://shells \
           file://issue.net \
           file://issue \
           file://passwd \
           file://shadow \
           file://group \
           "
S = "${WORKDIR}"

INHIBIT_DEFAULT_DEPS = "1"

docdir_append = "/${P}"
dirs1777 = "/tmp ${localstatedir}/volatile/tmp"
dirs555 = "/sys /proc"
dirs755 = "/boot /dev ${base_bindir} ${base_sbindir} ${base_libdir} \
           ${sysconfdir} ${sysconfdir}/default \
           ${sysconfdir}/skel ${nonarch_base_libdir} /mnt ${ROOT_HOME} /run \
           ${prefix} ${bindir} \
           ${libdir} ${sbindir} ${datadir} \
           ${localstatedir} \
           ${localstatedir}/lib \
           ${localstatedir}/volatile \
           ${localstatedir}/${@'volatile/' if oe.types.boolean('${VOLATILE_LOG_DIR}') else ''}log \
           /home ${prefix}/src ${localstatedir}/local \
           "

volatiles = "${@'log' if oe.types.boolean('${VOLATILE_LOG_DIR}') else ''} tmp"
conffiles = "${sysconfdir}/issue /${sysconfdir}/issue.net \
             ${sysconfdir}/profile \
             "

# By default the hostname is the machine name. If the hostname is unset then a
# /etc/hostname file isn't written, suitable for environments with dynamic
# hostnames.
hostname = "${MACHINE}"

BASEFILESISSUEINSTALL ?= "do_install_basefilesissue"

# In previous versions of base-files, /run was a softlink to /var/run and the
# directory was located in /var/volatlie/run.  Also, /var/lock was a softlink
# to /var/volatile/lock which is where the real directory was located.  Now,
# /run and /run/lock are the real directories.  If we are upgrading, we may
# need to remove the symbolic links first before we create the directories.
# Otherwise the directory creation will fail and we will have circular symbolic
# links.
# 
pkg_preinst_${PN} () {
    #!/bin/sh -e
    if [ x"$D" = "x" ]; then
        if [ -h "/var/lock" ]; then
            # Remove the symbolic link
            rm -f /var/lock
        fi

        if [ -h "/run" ]; then
            # Remove the symbolic link
            rm -f /run
        fi
    fi     
}

do_install () {
	for d in ${dirs555}; do
		install -m 0555 -d ${D}$d
	done
	for d in ${dirs755}; do
		install -m 0755 -d ${D}$d
	done
	for d in ${dirs1777}; do
		install -m 1777 -d ${D}$d
	done
	for d in ${volatiles}; do
		ln -sf volatile/$d ${D}${localstatedir}/$d
	done

	ln -snf ../run ${D}${localstatedir}/run
	ln -snf ../run/lock ${D}${localstatedir}/lock

	${BASEFILESISSUEINSTALL}

	install -o root -g root -p -m 644 ${WORKDIR}/passwd ${D}${sysconfdir}/passwd
	install -o root -g root -p -m 644 ${WORKDIR}/group ${D}${sysconfdir}/group
	install -m 0600 ${WORKDIR}/shadow ${D}${sysconfdir}/shadow
	install -m 0644 ${WORKDIR}/profile ${D}${sysconfdir}/profile
	sed -i 's#ROOTHOME#${ROOT_HOME}#' ${D}${sysconfdir}/profile
    sed -i 's#@BINDIR@#${bindir}#g' ${D}${sysconfdir}/profile
	install -m 0644 ${WORKDIR}/shells ${D}${sysconfdir}/shells

	ln -sf /proc/mounts ${D}${sysconfdir}/mtab
}

DISTRO_VERSION[vardepsexclude] += "DATE"
do_install_basefilesissue () {
	if [ "${hostname}" ]; then
		echo ${hostname} > ${D}${sysconfdir}/hostname
	fi

	install -m 644 ${WORKDIR}/issue*  ${D}${sysconfdir}
        if [ -n "${DISTRO_NAME}" ]; then
		printf "${DISTRO_NAME} " >> ${D}${sysconfdir}/issue
		printf "${DISTRO_NAME} " >> ${D}${sysconfdir}/issue.net
		if [ -n "${DISTRO_VERSION}" ]; then
			distro_version_nodate=${@'${DISTRO_VERSION}'.replace('snapshot-${DATE}','snapshot').replace('${DATE}','')}
			printf "%s " $distro_version_nodate >> ${D}${sysconfdir}/issue
			printf "%s " $distro_version_nodate >> ${D}${sysconfdir}/issue.net
		fi
		printf "\\\n \\\l\n" >> ${D}${sysconfdir}/issue
		echo >> ${D}${sysconfdir}/issue
		echo "%h"    >> ${D}${sysconfdir}/issue.net
		echo >> ${D}${sysconfdir}/issue.net
 	fi
}
do_install_basefilesissue[vardepsexclude] += "DATE"

SYSROOT_DIRS += "${sysconfdir}/skel"
SYSROOT_DIRS += "${sysconfdir}"

PACKAGES = "${PN}-doc ${PN} ${PN}-dev ${PN}-dbg"
FILES_${PN} = "/"
FILES_${PN}-doc = "${docdir} ${datadir}/common-licenses"

PACKAGE_ARCH = "${MACHINE_ARCH}"

CONFFILES_${PN} = "${@['', '${sysconfdir}/hostname'][(d.getVar('hostname') != '')]} ${sysconfdir}/shells"
CONFFILES_${PN} += "${sysconfdir}/profile"
