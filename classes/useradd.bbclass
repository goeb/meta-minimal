
# Modified from original poky/meta/useradd.bbclass for using static user and group files (supplied by package base-files)
#
# This make bitbake ignore the following variables that some recipes fulfill:
#	GROUPADD_PARAM
#	USERADD_PARAM
#	GROUPMEMS_PARAM


# base-passwd-cross provides the default passwd and group files in the
# target sysroot, and shadow -native and -sysroot provide the utilities
# and support files needed to add and modify user and group accounts
DEPENDS_append_class-target = " base-files"

# This preinstall function can be run in four different contexts:
#
# a) Before do_install
# b) At do_populate_sysroot_setscene when installing from sstate packages
# c) As the preinst script in the target package at do_rootfs time
# d) As the preinst script in the target package on device as a package upgrade
#
useradd_preinst () {
OPT=""
SYSROOT=""

if test "x$D" != "x"; then
	# Installing into a sysroot
	SYSROOT="$D"
	OPT="--root $D"

	# user/group lookups should match useradd/groupadd --root
	export PSEUDO_PASSWD="$SYSROOT"
fi

}

useradd_sysroot () {
	# Pseudo may (do_prepare_recipe_sysroot) or may not (do_populate_sysroot_setscene) be running 
	# at this point so we're explicit about the environment so pseudo can load if 
	# not already present.
	export PSEUDO="${FAKEROOTENV} ${PSEUDO_SYSROOT}${bindir_native}/pseudo"

	# Explicitly set $D since it isn't set to anything
	# before do_prepare_recipe_sysroot
	D=${STAGING_DIR_TARGET}

	# base-passwd's postinst may not have run yet in which case we'll get called later, just exit.
	# Beware that in some cases we might see the fake pseudo passwd here, in which case we also must
	# exit.
	if [ ! -f $D${sysconfdir}/passwd ] ||
			grep -q this-is-the-pseudo-passwd $D${sysconfdir}/passwd; then
		exit 0
	fi

	useradd_preinst
}

# The export of PSEUDO in useradd_sysroot() above contains references to
# ${COMPONENTS_DIR} and ${PSEUDO_LOCALSTATEDIR}. Additionally, the logging
# shell functions use ${LOGFIFO}. These need to be handled when restoring
# postinst-useradd-${PN} from the sstate cache.
EXTRA_STAGING_FIXMES += "COMPONENTS_DIR PSEUDO_LOCALSTATEDIR LOGFIFO"

python useradd_sysroot_sstate () {
    scriptfile = None
    task = d.getVar("BB_CURRENTTASK")
    if task == "package_setscene":
        bb.build.exec_func("useradd_sysroot", d)
    elif task == "prepare_recipe_sysroot":
        # Used to update this recipe's own sysroot so the user/groups are available to do_install
        scriptfile = d.expand("${RECIPE_SYSROOT}${bindir}/postinst-useradd-${PN}")
        bb.build.exec_func("useradd_sysroot", d)
    elif task == "populate_sysroot":
        # Used when installed in dependent task sysroots
        scriptfile = d.expand("${SYSROOT_DESTDIR}${bindir}/postinst-useradd-${PN}")

    if scriptfile:
        bb.utils.mkdirhier(os.path.dirname(scriptfile))
        with open(scriptfile, 'w') as script:
            script.write("#!/bin/sh\n")
            bb.data.emit_func("useradd_sysroot", script, d)
            script.write("useradd_sysroot\n")
        os.chmod(scriptfile, 0o755)
}

do_prepare_recipe_sysroot[postfuncs] += "${SYSROOTFUNC}"
SYSROOTFUNC_class-target = "useradd_sysroot_sstate"
SYSROOTFUNC = ""

SYSROOT_PREPROCESS_FUNCS += "${SYSROOTFUNC}"

SSTATEPREINSTFUNCS_append_class-target = " useradd_sysroot_sstate"

do_package_setscene[depends] += "${USERADDSETSCENEDEPS}"
do_populate_sysroot_setscene[depends] += "${USERADDSETSCENEDEPS}"
USERADDSETSCENEDEPS_class-target = "${MLPREFIX}base-files:do_populate_sysroot_setscene pseudo-native:do_populate_sysroot_setscene"
USERADDSETSCENEDEPS = ""
