################################################################################
# Default image (developer image)
################################################################################
ARG WILDFLY_BASE_IMAGE

FROM ${WILDFLY_BASE_IMAGE} as dev

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


# Expose the ports we're interested in
EXPOSE 8080 9990 8787

CMD ["sh", "-c", "${WILDFLY_HOME}/bin/standalone.sh -P /appconfig/app.properties -b 0.0.0.0 -bmanagement 0.0.0.0 --debug *:8787"]

