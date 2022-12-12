import axios from "axios";
import React from "react";
import {BACKEND_URL, FIRST_PAGE, MAX_PAGE_SIZE} from "./constants";
import {Modal} from "antd";


export const findAllLabels = async () => {
    let response
    try {
        response = await axios.get(`${BACKEND_URL}/labels`, {
            params: {
                page: FIRST_PAGE,
                size: MAX_PAGE_SIZE
            }
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const findLabelById = async (labelId, params) => {
    let response
    try {
        response = await axios.get(`${BACKEND_URL}/labels/${labelId}`, {
            params: params
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}


export const saveLabel = async (body) => {
    let response
    try {
        response = await axios.post(`${BACKEND_URL}/labels`, body)
        return response.data
    } catch (error) {
        if (error.response && error.response.data.code === 2) {
            return {
                error: true,
                title: error.response.data.message,
                errorText: `Label: ${error.response.data.metadata.label}`
            }
        }
        return {error: true}
    }
}

export const changeLabel = async (labelId, body) => {
    let response
    try {
        response = await axios.put(`${BACKEND_URL}/labels/${labelId}`, body)
        return response.data
    } catch (error) {
        return {error: true}
    }
}


export const deleteLabelById = async (labelId) => {
    let response
    try {
        response = await axios.delete(`${BACKEND_URL}/labels/${labelId}`)
        return response.data
    } catch (error) {
        if (error.response && error.response.data.code === 2) {
            return {
                error: true,
                title: error.response.data.message,
                errorText: `Label: ${error.response.data.metadata.label}`
            }
        }
        return {error: true}
    }
}


export const findAllApplications = async () => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/applications`, {
            params: {
                page: FIRST_PAGE,
                size: MAX_PAGE_SIZE
            }
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const findAllTraces = async (params) => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/traces`, {
            params: params
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const getTestStatistics = async () => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/analytics/statistics/tests`)
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const getCountStatistics = async () => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/analytics/statistics/count`)
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const getTracesRate = async (params) => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/analytics/rate/traces`, {
            params: params
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const getAlertEventsRate = async (params) => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/analytics/rate/alert-events`, {
            params: params
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const getTracesDistribution = async (params) => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/analytics/distribution/traces`, {
            params: params
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const getAlertEventsDistribution = async (params) => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/analytics/distribution/alert-events`, {
            params: params
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const getLabelDistribution = async (params) => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/analytics/distribution/labels`, {
            params: {
                labelId: params
            }
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}


export const findAllTestRuns = async (params) => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/test-runs`, {
            params: {
                page: FIRST_PAGE,
                size: MAX_PAGE_SIZE,
                ...params
            }
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const findAllTests = async (params) => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/tests`, {
            params: {
                page: FIRST_PAGE,
                size: MAX_PAGE_SIZE,
                ...params
            }
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}


export const findAllAlerts = async () => {
    let response

    try {
        response = await axios.get(`${BACKEND_URL}/alerts`, {
            params: {
                page: FIRST_PAGE,
                size: MAX_PAGE_SIZE
            }
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const findAlertById = async (alertId, params) => {
    let response
    try {
        response = await axios.get(`${BACKEND_URL}/alerts/${alertId}`, {
            params: params
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}



export const saveAlert = async (body) => {
    let response
    try {
        response = await axios.post(`${BACKEND_URL}/alerts`, body)
        return response.data
    } catch (error) {
        if (error.response && error.response.data.code === 2) {
            return {
                error: true,
                title: error.response.data.message,
                errorText: `Alert: ${error.response.data.metadata.name}`
            }
        }
        return {error: true}
    }
}

export const changeAlert = async (alertId, body) => {
    let response
    try {
        response = await axios.put(`${BACKEND_URL}/alerts/${alertId}`, body)
        return response.data
    } catch (error) {
        return {error: true}
    }
}


export const deleteAlertById = async (alertId) => {
    let response
    try {
        response = await axios.delete(`${BACKEND_URL}/alerts/${alertId}`)
        return response.data
    } catch (error) {
        return {error: true}
    }
}

export const findAllAlertEvents = async (alertId) => {
    let response
    try {
        response = await axios.get(`${BACKEND_URL}/alert-events?alertId=${alertId}`, {
            params: {
                page: FIRST_PAGE,
                size: MAX_PAGE_SIZE
            }
        })
        return response.data
    } catch (e) {
        return {error: true}
    }
}

export const changeAlertEvent = async (alertEventId, body) => {
    let response
    try {
        response = await axios.put(`${BACKEND_URL}/alert-events/${alertEventId}`, body)
        return response.data
    } catch (error) {
        return {error: true}
    }
}
