# docker-compose adja
ARG WILDFLY_BASE_IMAGE

################################################################################
# Default image customization
################################################################################
FROM ${WILDFLY_BASE_IMAGE} as base

ARG WILDFLY_CLI_PATH
ARG TMP_DIR=$HOME/tmp

#embed server scipts
COPY --chown=$SYSTEM_USER:$SYSTEM_USER_GROUP \
    ${WILDFLY_CLI_PATH} \
    $TMP_DIR/embed-server/

RUN set -x; \
    echo "TMP_DIR=$TMP_DIR" >> $TMP_DIR/wf.properties && \
    $WILDFLY_HOME/bin/jboss-cli.sh \
        --file=$TMP_DIR/embed-server/embed-server.cli \
        --properties=$TMP_DIR/wf.properties && \
    rm -rf ${TMP_DIR} && \
    rm -rf $WILDFLY_HOME/standalone/configuration/standalone_xml_history


################################################################################
# Create production image
################################################################################
FROM base as prod

ARG POM_ARTIFACT_ID
ARG POM_VERSION
ARG POM_EXTENSION

ARG DOWNLOAD_DIR_NAME
ARG DOWNLOAD_DIR=$HOME/$DOWNLOAD_DIR_NAME
ARG PROPERTIES_FILE

LABEL moduleName="$POM_ARTIFACT_ID"
LABEL moduleVersion="$POM_VERSION"

ENV LAUNCH_JBOSS_IN_BACKGROUND true
LABEL jbossbackground="true"

RUN echo "$HOME" && \
    echo "$DOWNLOAD_DIR/$OBJECT_DOWNLOAD_OUTPUT_FILE" && \
    echo "$POM_ARTIFACT_ID" && \
    echo "$POM_VERSION" && \
    # Adding deployment artifact as ROOT.war and prepare to auto deploy
    touch ${WILDFLY_HOME}/standalone/deployments/ROOT.war.dodeploy
    
COPY --chown=$SYSTEM_USER:$SYSTEM_USER_GROUP $PROPERTIES_FILE $WILDFLY_PROPERTIES_FILE

#copy war
COPY --chown=$SYSTEM_USER:$SYSTEM_USER_GROUP /sample/sample-rest-service/target/sample-rest-service-0.1.0-SNAPSHOT.war $WILDFLY_HOME/standalone/deployments/ROOT.war


CMD exec "${WILDFLY_HOME}/bin/standalone.sh" -P $WILDFLY_PROPERTIES_FILE -b 0.0.0.0 -bmanagement 0.0.0.0 --debug *:8787

#COPY --from=download --chown=$SYSTEM_USER:$SYSTEM_USER_GROUP $DOWNLOAD_DIR/*.$POM_EXTENSION $WILDFLY_HOME/standalone/deployments/ROOT.war
