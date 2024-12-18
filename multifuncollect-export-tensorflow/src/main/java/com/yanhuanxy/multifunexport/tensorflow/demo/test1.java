//package com.yanhuanxy.multifunexport.tensorflow.demo;
//
//import org.tensorflow.*;
//import org.tensorflow.ndarray.LongNdArray;
//import org.tensorflow.ndarray.NdArrays;
//import org.tensorflow.ndarray.Shape;
//import org.tensorflow.op.Ops;
//import org.tensorflow.op.core.Placeholder;
//import org.tensorflow.proto.framework.MetaGraphDef;
//import org.tensorflow.proto.framework.SignatureDef;
//import org.tensorflow.types.TFloat32;
//import org.tensorflow.types.TInt32;
//import org.tensorflow.types.TInt64;
//
//import java.io.IOException;
//import java.util.Map;
//
//public class test1 {
//    public static void main(String[] args) throws IOException {
//        // 设置默认设备类型为GPU // 0表示第一个GPU，1表示第二个GPU，以此类推
//        System.setProperty("tensorflow.device.gpu", "0");
//        // 或者设置默认设备类型为CPU
////        System.setProperty("tensorflow.device.cpu", "true");
//        // 设置默认日志级别为WARN
//        System.setProperty("org.tensorflow.LogLevel", "3");
//        // 设置默认的模型保存路径
//        System.setProperty("tensorflow.models.dir", "/path/to/models");
//
//        TensorFlow.loadLibrary("tensorflow_jni");
//
//
//        try(Graph graph = new Graph()) {
//            Ops ops = Ops.create(graph);
//
//            // Define the variables for the model
//            TFloat32 W = ops.variable(ops.constant(0.0f), TFloat32.class);
//            TFloat32 b = ops.variable(ops.constant(0.0f), TFloat32.class);
//
//            // Define the input and output placeholders
//            TFloat32 X = ops.placeholder(TFloat32.class);
//            TFloat32 Y = ops.placeholder(TFloat32.class);
//
//            // Define the model
//            TFloat32 Y_pred = ops.add(ops.matmul(X, W), b);
//
//            // Define the loss function
//            TFloat32 loss = ops.mean(ops.square(ops.sub(Y_pred, Y)), ops.constant(0));
//
//            // Define the optimizer
//            org.tensorflow.op.train.GradientDescent optimizer = new org.tensorflow.op.train.GradientDescent(graph, 0.01f);
//            org.tensorflow.op.train.Optimizer.minimize(optimizer, loss, ops.constant(0));
//
//            // Create a TensorFlow session and initialize the variables
//            try (Session session = new Session(graph)) {
//                session.run(ops.variablesInitializer().call());
//
//                // Train the model
//                for (int i = 0; i < 100; i++) {
//                    float[] x = {1.0f, 2.0f, 3.0f, 4.0f};
//                    float[] y = {2.0f, 4.0f, 6.0f, 8.0f};
//
//                    session.runner()
//                            .feed(X.asOutput(), TFloat32.tensorOf(ops, x, 4))
//                            .feed(Y.asOutput(), TFloat32.tensorOf(ops, y, 4))
//                            .addTarget(optimizer.minimize())
//                            .run();
//                }
//
//                // Predict using the model
//                float[] x = {5.0f};
//                Tensor<Float> input = TFloat32.tensorOf(ops, x, 1);
//                Tensor<Float> output = session.runner()
//                        .feed(X.asOutput                (), input)
//                        .fetch(Y_pred.asOutput())
//                        .run()
//                        .get(0)
//                        .expect(Float.class);
//                System.out.println("Predicted value: " + output);
//            }
//        }
//
//
//
//    }
//
//    /**
//     * 调用 模型
//     *
//     * @throws IOException
//     */
//    private void executeModule() throws IOException{
//        // 加载模型
//        SavedModelBundle savedModelBundle = SavedModelBundle.load("\"C:\\\\Users\\\\Admin\\\\Desktop\\\\xxx_savedmodel", "server");
//        Map<String, SignatureDef> signatureDefMap = MetaGraphDef.parseFrom(savedModelBundle.metaGraphDef().toByteArray()).getSignatureDefMap();
//        /**
//         * 获取基本定义信息
//         */
//        SignatureDef modelSig = signatureDefMap.get("serving_default");
//        int numInputs = modelSig.getInputsCount();
//        String inputTensorName = modelSig.getInputsMap().get("input_1").getName();
//        String outputTensorName = modelSig.getOutputsMap().get("dense_3").getName();
//        System.out.printf("numInputs: %d, inputTensorName: %s, outputTensor: %s%n", numInputs, inputTensorName, outputTensorName);
//
//
//        try (Graph g = new Graph(); Session session = new Session(g)) {
//            LongNdArray matrix3d = NdArrays.ofLongs(Shape.of(1,100,9));
//            //假定参数
//            matrix3d.elements(0).forEach(matrix -> {
//                matrix.set(NdArrays.vectorOf(1l, 2l,3l,2l,3l,2l,3l,2l,3l), 0)
//                        .set(NdArrays.vectorOf(3l, 4l,6l,2l,3l,2l,3l,2l,3l), 1);
//            });
//            // 创建一个常量张量
//            TInt64 rank3Tensor = TInt64.vectorOf(matrix3d.getLong(0));
//
//            Result run = session.runner().feed(inputTensorName, rank3Tensor).fetch(outputTensorName).run();
//            Tensor result = run.get(0);
//            System.out.println(result.shape());
//        }
//    }
//
//    /**
//     * 构建 训练模型
//     */
//    private void trainModule(){
//        try (Graph graph = new Graph(); Session session = new Session(graph)) {
//            Ops tf = Ops.create(graph);
//            // 创建输入占位符
//            Placeholder<TInt32> input = tf.placeholder(TInt32.class, Placeholder.shape(Shape.of(-1, 1)));
//
//            // 构建模型
//            Output<TFloat32> output = buildModel(tf, input);
//
//            // 定义损失函数
//            Placeholder<TFloat32> labels = tf.placeholder(TFloat32.class);
//            Output<TFloat32> loss = calculateLoss(tf, output, labels);
//
//            // 创建优化器
//            float learningRate = 0.01f;
//            Output<TFloat32> trainOp = createOptimizer(tf, loss, learningRate);
//
//            // 初始化变量
//            session.runner().addTarget(tf.init()).run();
//
//            // 训练模型
//            TFloat32 inputData = Tensors.create(new float[]{1, 2, 3, 4});
//            TFloat32 labelData = Tensors.create(new float[]{4, 9, 16, 25});
//            trainModel(session, tf, input, labels, trainOp, inputData, labelData);
//
//            // 预测
//            TFloat32 predictData = Tensors.create(new float[]{5, 6, 7, 8});
//            float[] predictions = predict(session, tf, input, output, predictData);
//
//            for (float prediction : predictions) {
//                System.out.println("Prediction: " + prediction);
//            }
//        }
//
//    }
//
//    private static Output<TFloat32> buildModel(Ops tf, Output<TInt32> input) {
//        // 构建模型，这里简单地执行平方差操作
//
//        Output<TFloat32> squaredDifference = tf.math.squaredDifference(tf.dtypes.cast(input, TFloat32.class),
//                tf.constant(TFloat32.class, 5.0f));
//        return squaredDifference;
//    }
//
//    private static Output<TFloat32> calculateLoss(Ops tf, Output<TFloat32> predictions, Placeholder<TFloat32> labels) {
//        // 定义损失函数，这里使用均方差损失
//        Output<TFloat32> loss = tf.math.mean(tf.math.squaredDifference(predictions, labels),
//                tf.constant(TFloat32.class,0));
//        return loss;
//    }
//
//    private static Output<TFloat32> createOptimizer(Ops tf, Output<TFloat32> loss, float learningRate) {
//        // 创建优化器，这里使用随机梯度下降 (SGD) 优化器
//        return tf.train.applyGradientDescent(loss, learningRate);
//    }
//
//    private static void trainModel(Session session, Ops tf, Placeholder<TInt32> input, Placeholder<TFloat32> labels,
//                                   Output<TFloat32> trainOp, Tensor<TFloat32> inputData, Tensor<TFloat32> labelData) {
//        for (int i = 0; i < 100; i++) {
//            session.runner().feed(input.asOutput(), inputData).feed(labels.asOutput(), labelData).addTarget(trainOp).run();
//        }
//    }
//
//    private static float[] predict(Session session, Ops tf, Placeholder<TInt32> input, Output<TFloat32> output, Tensor<TFloat32> inputData) {
//        return session.runner().feed(input.asOutput(), inputData).fetch(output).run().get(0).expect(TFloat32.DTYPE).copyTo(new float[4]);
//    }
//
//}
