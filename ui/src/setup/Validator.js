

export const validateSequence = (data) => {
        let{sequence} = data;
        if (!sequence){
            sequence = data;
        }
        if (!/^[01]+$/.test(sequence) || sequence.length < 3){
            return "Послідовність повинна бути довжиною щонайменше 3 і складатися тільки з '1' та '0'";
        }else{
            return false
        }
}


export const validateWithBlock = (data) => {
    let firstValidation = validateSequence(data);
    if (firstValidation) {
        return firstValidation
    } else {
        const {sequence, blockSize} = data;
        if (!/^[1-9][0-9]*$/.test(blockSize)){
            return "Довжина блоку має бути цілим числом"
        }
        if (parseInt(blockSize) > sequence.length || parseInt(blockSize) < 2) {
            return "Довжина блоку не повинна перевищувати розмір послідовності та бути більше ніж 1"
        }

        return false
    }
}
export const validateWithMatrix = (data) => {
    let firstValidation = validateSequence(data);
    if (firstValidation) {
        return firstValidation
    } else {
        const {sequence, matrixSize} = data;
        if (!/^[1-9][0-9]*$/.test(matrixSize)){
            return "Розмір сторони матриці має бути цілим числом"
        }
        if (parseInt(matrixSize) > sequence.length || parseInt(matrixSize) < 2 || Math.pow(parseInt(matrixSize),2)>sequence.length) {
            return "Розмір матриці повинен бути меншим ніж "+Math.floor(Math.sqrt(sequence.length))+" та більшим ніж 1"
        }
        return false
    }
}
export const validateSerial = (data) =>{
    let firstValidation = validateSequence(data);
    if (firstValidation) {
        return firstValidation
    } else {
        const {sequence, blockSize} = data;
        if (!/^[1-9][0-9]*$/.test(blockSize)){
            return "Довжина блоку має бути цілим числом"
        }
        if (parseInt(blockSize) > sequence.length || parseInt(blockSize) < 3 || parseInt(blockSize) > 15) {
            return "Довжина блоку має бути більшою ніж 2 та меншою ніж 16"
        }
        return false
    }
}

export const validateEntropy = (data) =>{
    let firstValidation = validateSequence(data);
    if (firstValidation) {
        return firstValidation
    } else {
        const {sequence, blockSize} = data;
        if (!/^[1-9][0-9]*$/.test(blockSize)){
            return "Довжина блоку має бути цілим числом"
        }
        if (parseInt(blockSize) > sequence.length || parseInt(blockSize) < 2 || parseInt(blockSize) > 14) {
            return "Довжина блоку має бути більшою ніж 1 та меншою ніж 15"
        }
        return false
    }
}
export const validateWithTemplate = (data) => {
    let secondValidation = validateWithBlock(data);
    if (secondValidation) {
        return secondValidation
    } else {
        const {blockSize, template} = data;
        if (!/^[01]+$/.test(template)) {
            return "Шаблон повинен складатися тільки з '1' та '0'"
        } else if (parseInt(blockSize) < template.length || template.length < 2) {
            return "Довжина шаблону має бути менше ніж довжина блоку та більшою ніж 1"
        }
        return false
    }
}
export const validateMaurers = (data) => {
    let firstValidation = validateSequence(data);
    if (firstValidation) {
        return firstValidation
    } else {
        const {sequence, blockSize, blocksInInitSegment} = data;
        if (!/^[1-9][0-9]*$/.test(blockSize)){
            return "Довжина блоку має бути цілим числом"
        } else if (!/^[1-9][0-9]*$/.test(blocksInInitSegment)){
            return "Кількість блоків в початковому сегменті має бути цілим числом"
        }
        if (parseInt(blockSize) > sequence.length/10 || parseInt(blockSize) < 2 || parseInt(blockSize) > 16) {
            return "Довжина блоку повинна бути меншою ніж послідовність щонайменше в 10 разів, менше 16 та більше 1"
        }
        if (parseInt(blockSize) * parseInt(blocksInInitSegment) > sequence.length / 2 || parseInt(blocksInInitSegment) < 1) {
            return "Довжина початкового сегменту повинна бути менше ніж " + Math.ceil(sequence.length / 2 / parseInt(blockSize)) +
                " та більше нуля"
        }
        return false
    }
}



export default {validateSequence, validateWithBlock, validateWithTemplate, validateWithMatrix, validateMaurers}
