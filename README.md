<div dir="rtl">

# پیوست

## قضیه‌ی مقدار میانگین

 اگر تابعی در بازه‌ی [a, b] پیوسته و هم‌چنین در بازه‌ی (a, b) مشتق‌پذیر باشد، آن‌گاه داریم:
</div>

$$c\prime \in (a, b) $$
$$\frac{f(b) - f(a)}{b - a} = f(c\prime)$$

<div dir="rtl">

## قضیه پیوستگی لیپ‌شیتس (Lipschitz continuity)

فرض کنید تابع یک متغیره $(f(x$ که $x$ در آن عضو دامنه‌ی $D$ است. اندازه‌ی شیب دو نقطه‌ی $b$ و $a$ از دامنه‌ی $D$ به صورت زیر است:
</div>

$$|\frac{f(b) - f(a)}{b - a}
|$$

<div dir="rtl">
حال، عدد حقیقی نامنفی مانند $C$وجود دارد که کوچک‌ترین کران بالا برای شیب آن است و به آن عدد ثابت لیپ‌شیتس گفته می‌شود.  به بیان ریاضی:
</div>

$$C = sup|\frac{f(b) - f(a)}{b - a}
| = sup |\frac{\partial f}{\partial x}|$$

$$ |f(b) - f(a)| \leq C |b - a|$$

<div dir="rtl">

## ماتریس Hessian

 یک ماتریس مربعی است که شامل مشتقات جزئی مرتبه دوم یک تابع باشد. این ماتریس در واقع بیان گر میزان انحنای موضعی تابع مورد نظر به ازای متغیرهای آن است.
</div>

$$
H(f) = \begin{bmatrix}
\frac{\partial^2 f}{\partial x_1^2} & \frac{\partial^2 f}{\partial x_1\,\partial x_2} & \cdots & \frac{\partial^2 f}{\partial x_1\,\partial x_n} \\  \\
\frac{\partial^2 f}{\partial x_2\,\partial x_1} & \frac{\partial^2 f}{\partial x_2^2} & \cdots & \frac{\partial^2 f}{\partial x_2\,\partial x_n} \\  \\
\vdots & \vdots & \ddots & \vdots \\  \\
\frac{\partial^2 f}{\partial x_n\,\partial x_1} & \frac{\partial^2 f}{\partial x_n\,\partial x_2} & \cdots & \frac{\partial^2 f}{\partial x_n^2}
\end{bmatrix}.
$$